package com.ruoyi.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.AttachmentConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.login.utils.AlRedisUntil;
import com.ruoyi.system.roles.entity.SysUserRole;
import com.ruoyi.system.roles.service.SysUserRoleService;
import com.ruoyi.system.user.dto.UserDto;
import com.ruoyi.system.user.entity.ArchiveEntity;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.mapper.UserMapper;
import com.ruoyi.system.user.service.ArchiveService;
import com.ruoyi.system.user.service.UserService;
import com.ruoyi.system.user.vo.PasswordVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.apache.shiro.SecurityUtils.getSubject;

/**
 * @author ZhuYiCheng
 * @date 2023/4/26 10:07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private ArchiveService archiveService;


    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private AlRedisUntil alRedisUntil;


    /**
     * 校验工号唯一
     *
     * @param jobNum
     * @return
     */
    @Override
    public Boolean checkJobNum(String jobNum) {
        List<UserEntity> userEntityList = this.list(new QueryWrapper<UserEntity>()
                .eq(StringUtils.isNotBlank(jobNum), "job_num", jobNum)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        if (userEntityList.size() != 0) {
            return false;
        }
        return true;
    }

    /**
     * 校验邮箱唯一
     *
     * @param email
     * @return
     */
    @Override
    public Boolean checkEmail(String email) {
        List<UserEntity> userEntityList = this.list(new QueryWrapper<UserEntity>()
                .eq(StringUtils.isNotBlank(email), "email", email)
                .eq("del_flag", DeleteFlag.NO.ordinal()));

        if (userEntityList.size() != 0) {
            return false;
        }
        return true;
    }

    /**
     * 发送邮件
     *
     * @param email 邮件信息
     */
    public void sendSimpleMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("创建用户成功");
        message.setText("您好:\r\n" +
                "\r\n您的账号为：" + email + "，初始密码为：ly123456，\r\n" +
                "\r\n可通过电脑浏览访问 http://60.190.21.178:10090 登录并修改密码\r\n");
        message.setTo(email);
        javaMailSender.send(message);
    }

    /**
     * 新增用户
     *
     * @param userEntity
     * @return
     */
    @Override
    @Transactional
    public Boolean addUser(UserEntity userEntity) {
        Date nowDate = DateUtils.getNowDate();

        userEntity.setCreateTime(nowDate);
        // sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        userEntity.setPassword(new Sha256Hash("ly123456", salt).toHex());
        userEntity.setSalt(salt);
        boolean b = this.save(userEntity);
        UserEntity userEntity1 = this.getOne(new QueryWrapper<UserEntity>()
                .eq("username", userEntity.getUsername())
                .eq("mobile", userEntity.getMobile()));
        // 保存用户与角色关系
        List<Long> roleIdList = userEntity.getRoleIdList();
        List<SysUserRole> userRoleList = new ArrayList<>();
        //校验参数不为空
        if (roleIdList == null || roleIdList.size() > 0) {
            for (Long roleId : roleIdList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userEntity1.getUserId());
                sysUserRole.setRoleId(roleId);
                userRoleList.add(sysUserRole);
            }
            sysUserRoleService.saveBatch(userRoleList);
        }

        //发送邮件通知   您好，您的账号为：XXXX，初始密码为：XXXX，可通过电脑浏览访问XXXX登录并修改密码
        sendSimpleMail(userEntity.getEmail());

        return b;
    }


    /**
     * 所有用户列表
     *
     * @return
     */
    @Override
    public List<UserEntity> getUserList(UserDto userDto) {

        String jobNum = userDto.getJobNum();
        String username = userDto.getUsername();
        String status = userDto.getStatus();

        pageUtil.startPage();
        List<UserEntity> list = this.list(new QueryWrapper<UserEntity>()
                .like(StringUtils.isNotBlank(jobNum), "job_num", jobNum)
                .like(StringUtils.isNotBlank(username), "username", username)
                .eq(StringUtils.isNotBlank(status), "status", status)
                .eq("del_flag", DeleteFlag.NO.ordinal())
                .orderByDesc("create_time"));

        for (UserEntity userEntity : list) {

            List<String> roleList = userMapper.getRoleList(userEntity.getUserId());
            userEntity.setRoleList(roleList);
            //为了安全将密码与加密盐隐藏不返回页面
            userEntity.setPassword(null);
            userEntity.setSalt(null);
        }

        return list;
    }


    /**
     * 显示所有用户列表(不分页,用于下列选择用户,不含中介)
     *
     * @return
     */
    @Override
    public List<UserEntity> getListAll() {

        return this.list(new QueryWrapper<UserEntity>()
                        .select("user_id", "username", "job_num", "dept_id", "subjection", "type", "email")
//                .gt("user_id", 1)
                        .eq("type", 1)
                        .eq("status", UserConstants.NORMAL)
                        .eq("del_flag", DeleteFlag.NO.ordinal())
        );
    }

    /**
     * 根据id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity getDetail(Long userId) {
        UserEntity userEntity = userMapper.selectById(userId);
        //为了安全将密码与加密盐隐藏不返回页面
//        userEntity.setPassword(null);
//        userEntity.setSalt(null);
        //查询用户角色id集合
        List<Long> roleIdList = userMapper.getRoleIdList(userEntity.getUserId());
        userEntity.setRoleIdList(roleIdList);
        return userEntity;
    }


    /**
     * 修改用户信息
     *
     * @param userEntity
     */
    @Override
    @Transactional
    public Boolean updateUser(UserEntity userEntity) {

        if (StringUtils.isNotBlank(userEntity.getPassword())) {
            // sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            //修改密码时重新生成加密盐
            userEntity.setPassword(new Sha256Hash(userEntity.getPassword(), salt).toHex());
            userEntity.setSalt(salt);
            userEntity.setChangeNumber(userEntity.getChangeNumber() + 1);
        }
        //修改用户信息
        userMapper.updateById(userEntity);
        // 刷新token信息和UserEntity中用户信息
        RefreshTokenAndEntity(userEntity);
        // 保存用户与角色关系
        List<Long> roleIdList = userEntity.getRoleIdList();
        //先删除原来的用户与角色关系
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userEntity.getUserId()));
        //保存新的用户与角色关系
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userEntity.getUserId());
            sysUserRole.setRoleId(roleId);
            userRoleList.add(sysUserRole);
        }
        sysUserRoleService.saveBatch(userRoleList);
        return true;
    }


    /**
     * 修改用户状态(status状态  0：正常   1：禁用)
     *
     * @param userId
     * @param status
     */
    @Override
    public void updateStatus(Long userId, Integer status) {
        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(status);
        //解锁用户时，失败次数改为0
        if (Objects.equals(status, UserConstants.NORMAL)) {
            userEntity.setDefeats(0);
        }

        userMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("user_id", userId));
    }


    /**
     * 修改用户登录密码
     *
     * @param passwordVo
     * @return
     */
    @Override
    public Boolean updatePassword(PasswordVo passwordVo) {
        UserEntity userEntity = (UserEntity) getSubject().getPrincipal();
        //sha256加密
        String password = new Sha256Hash(passwordVo.getPassword(), userEntity.getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordVo.getNewPassword(), userEntity.getSalt()).toHex();
        //设置新密码和密码修改次数+1
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setPassword(newPassword);
        userEntity1.setChangeNumber(userEntity.getChangeNumber() + 1);
        return this.update(userEntity1, new QueryWrapper<UserEntity>().
                eq("user_id", userEntity.getUserId()).eq("password", password));
    }

    /**
     * 删除用户信息(逻辑删除)
     *
     * @param userId
     */
    @Override
    public void deleteUser(Long userId) {
        UserEntity userEntity = new UserEntity();
        //修改逻辑删除字段的值
        userEntity.setDelFlag(DeleteFlag.YES.ordinal());
        //修改用户状态为禁用状态
        userEntity.setStatus(UserConstants.EXCEPTION);
        userMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("user_id", userId));
        // 删除用户角色关联关系
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //逻辑用户所属档案信息,设置为临时文件，后续自动清理
        archiveService.update(new UpdateWrapper<ArchiveEntity>()
                .eq("parent_id",userId)
                .set("temp_id", AttachmentConstants.TEMPORARY)
                .set("del_flag",DeleteFlag.YES.ordinal()));
        //清理redis中存在的缓存
        RemoveTokenAndEntity(userId);
    }

    /**
     * 刷新用户token和entity
     */
    private void RefreshTokenAndEntity(UserEntity user){
        int EXPIRE = 1800 ;
        Object pcToken = alRedisUntil.get(Constants.ACCESS_USERID + user.getUserId() + "_pc");
        Object wxToken = alRedisUntil.get(Constants.ACCESS_USERID + user.getUserId() + "_wx");
        if (pcToken != null){
            //刷新token
            alRedisUntil.expire(Constants.ACCESS_USERID + user.getUserId(), EXPIRE);
            //更新redis 存储最新Entity
            String pcTokenString = alRedisUntil.toJson(pcToken);
            alRedisUntil.set(Constants.ACCESS_TOKEN + pcTokenString, user);
        }
        if (wxToken != null){
            //刷新token
            alRedisUntil.expire(Constants.ACCESS_USERID + user.getUserId(), EXPIRE);
            //更新redis 存储最新Entity
            String pcTokenString = alRedisUntil.toJson(wxToken);
            alRedisUntil.set(Constants.ACCESS_TOKEN + pcTokenString, user);
        }
    }

    /**
     * 删除用户token和entity
     */
    private void RemoveTokenAndEntity(Long id){
        Object pcToken = alRedisUntil.get(Constants.ACCESS_USERID + id + "_pc");
        Object wxToken = alRedisUntil.get(Constants.ACCESS_USERID + id + "_wx");
        if (pcToken != null){
            alRedisUntil.del(Constants.ACCESS_USERID + id, Constants.ACCESS_TOKEN + alRedisUntil.toJson(pcToken));
        }
        if (wxToken != null){
            alRedisUntil.del(Constants.ACCESS_USERID + id, Constants.ACCESS_TOKEN + alRedisUntil.toJson(wxToken));
        }
    }
}
