package com.ruoyi.system.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.exception.RRException;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.user.dto.UserDto;
import com.ruoyi.system.user.entity.UserEntity;
import com.ruoyi.system.user.service.ArchiveService;
import com.ruoyi.system.user.service.UserService;
import com.ruoyi.system.user.vo.PasswordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.system.login.utils.ShiroUtils.getUserId;


/**
 * @author ZhuYiCheng
 * @date 2023/4/26 9:46
 */
@RestController
@Api("用户管理")
@RequestMapping("/sys/user")
public class UserController {

    private final ArchiveService archiveService;
    private UserService userService;

    @Autowired
    public UserController(ArchiveService archiveService, UserService userService) {
        this.archiveService = archiveService;
        this.userService = userService;
    }


    /**
     * 校验工号唯一
     *
     * @param jobNum
     * @return
     */
    @ApiOperation("校验工号唯一")
    @GetMapping("/checkJobNum/{jobNum}")
    public R checkJobNum(@PathVariable("jobNum") String jobNum) {
        Boolean b = userService.checkJobNum(jobNum);
        return b ? R.ok() : R.error("用户工号不能重复");
    }

    /**
     * 校验邮箱唯一
     *
     * @param email
     * @return
     */
    @ApiOperation("校验邮箱唯一")
    @GetMapping("/checkEmail/{email}")
    public R checkEmail(@PathVariable("email") String email) {
        Boolean b = userService.checkEmail(email);
        return b ? R.ok() : R.error("用户邮箱不能重复");
    }

    /**
     * 新增用户
     *
     * @param userEntity
     * @return
     */
    @ApiOperation("新增用户")
    @OperateLog(title = "新增用户")
    @PostMapping("/addUser")
    @RepeatSubmit
    public R addUser(@RequestBody UserEntity userEntity) {

//        UserEntity userEntity = userVo.getUserEntity();
        //填充信息
        userEntity.setCreateUserId(getUserId());
        //保存用户信息
        Boolean b = userService.addUser(userEntity);

        UserEntity userEntity1 = userService.getOne(new QueryWrapper<UserEntity>()
                .eq("email", userEntity.getEmail())
                .eq("del_flag",DeleteFlag.NO.ordinal()));
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userEntity1.getUserId());
        map.put("username",userEntity1.getUsername());

        return b ? R.ok("新增用户成功",map) : R.error("新增用户失败");
    }


    /**
     * 所有用户列表
     *
     * @param userDto
     * @return
     */
    @ApiOperation("所有用户列表")
    @GetMapping("/list")
    public R getUserList(UserDto userDto) {
        List<UserEntity> userList = userService.getUserList(userDto);
        return R.resultData(userList);
    }


    /**
     * 显示所有用户列表(不分页,用于下列选择用户)
     *
     * @return
     */
    @ApiOperation("显示所有用户列表(不分页,用于下列选择用户)")
    @GetMapping("/listAll")
    public R getListAll() {
        List<UserEntity> userEntityList = userService.getListAll();
        return R.ok().put("list", userEntityList);
    }


    /**
     * 查看用户详情
     *
     * @param userId
     * @return
     */
    @ApiOperation("查看用户详情")
    @GetMapping("/getDetail/{userId}")
    public R getDetail(@PathVariable("userId") Long userId) {
        UserEntity user = userService.getDetail(userId);

        return R.ok().put("user", user);
    }


    /**
     * 修改用户信息
     *
     * @param userEntity
     * @return
     */
    @ApiOperation("修改用户信息")
    @OperateLog(title = "修改用户信息")
    @PutMapping("/updateUser")
    public R updateUser(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userService.getById(userEntity.getUserId());
        String jobNum = userEntity.getJobNum();
        String jobNum1 = userEntity1.getJobNum();
        //如果工号修改过
        if (!jobNum.equals(jobNum1)) {
            //校验工号是否唯一
            List<UserEntity> userEntityList = userService.list(new QueryWrapper<UserEntity>()
                    .eq(StringUtils.isNotBlank(jobNum), "job_num", jobNum)
                    .eq("del_flag", DeleteFlag.NO.ordinal()));
            if (userEntityList.size() != 0) {
                return R.error("用户工号不能重复");
            }
        }
        String email = userEntity.getEmail();
        String email1 = userEntity1.getEmail();
        //如果邮箱修改过
        if (!email.equals(email1)) {
            //校验邮箱是否唯一
            List<UserEntity> userEntityList = userService.list(new QueryWrapper<UserEntity>()
                    .eq(StringUtils.isNotBlank(email), "email", email)
                    .eq("del_flag", DeleteFlag.NO.ordinal()));

            if (userEntityList.size() != 0) {
                return R.error("用户邮箱不能重复");
            }
        }

        Boolean b = userService.updateUser(userEntity);
        return b ? R.ok("修改用户成功") : R.error("修改用户失败");
    }


    /**
     * 修改用户状态(status状态  0：禁用   1：正常)
     *
     * @param userId
     * @param status
     * @return
     */
    @ApiOperation("修改用户状态(status状态  0：禁用   1：正常)")
    @OperateLog(title = "修改用户状态(status状态  0：禁用   1：正常)")
    @PutMapping("/updateStatus")
    public R updateStatus(@RequestParam Long userId, @RequestParam Integer status) {

        userService.updateStatus(userId, status);
        return R.ok();
    }


    /**
     * 修改用户密码
     *
     * @param passwordVo
     * @return
     */
    @ApiOperation("修改用户密码")
    @OperateLog(title = "修改用户密码",isSaveRequestData = false)
    @PutMapping("/updatePassword")
    public R updatePassword(@RequestBody PasswordVo passwordVo) {
        if (StringUtils.isBlank(passwordVo.getNewPassword())) {
            throw new RRException("新密码不为能空");
        }
        Boolean b = userService.updatePassword(passwordVo);

        if (!b) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }


    /**
     * 删除用户信息(逻辑删除)
     *
     * @param userId
     * @return
     */
    @ApiOperation("删除用户信息(逻辑删除)")
    @OperateLog(title = "删除用户信息(逻辑删除)")
    @PutMapping("/deleteUser")
    public R deleteUser(@RequestParam("userId") Long userId) {
        //用户逻辑删除
        userService.deleteUser(userId);
        //TODO 是否用户删除同时删除用户登录信息
        return R.ok();
    }


    /**
     * 查询用户 用于下拉选择
     * @param name
     * @return
     */
    @GetMapping("/userByName")
    public R userByName(String name) {
        List<UserEntity> list = userService.list(new LambdaQueryWrapper<UserEntity>().like(StrUtil.isNotBlank(name),UserEntity::getUsername,name).select(UserEntity::getUserId, UserEntity::getUsername).last(" limit 30 "));
        return R.data(list);
    }

}
