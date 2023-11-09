package com.ruoyi.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/26 9:26
 */
@Data
@TableName("sys_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long userId;
    /**
     * 用户名
     */
//    @NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    /**
     * 昵称
     */
    private String name;
    /**
     * 性别（1男，2女）
     */
    private Integer sex;
    /**
     * 出生年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthdate;
    /**
     * 工号
     */
    private String jobNum;
//    /**
//     * 工号是否修改
//     */
//    @TableField(exist=false)
//    private Boolean isUpdateJobNum;
    /**
     * 密码
     */
//    @NotBlank(message="密码不能为空", groups = AddGroup.class)
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
//    /**
//     * 邮箱是否修改
//     */
//    @TableField(exist=false)
//    private Boolean isUpdateEmail;
    /**
     * 居住地址
     */
    private String residentialAddress;
    /**
     * 紧急联系人
     */
    private String emergencyContacts;
    /**
     * 联系人关系(配偶、父亲、母亲、子女、兄弟姐妹、其他亲属)
     */
    private String relationship;
    /**
     * 联系人手机
     */
    private String contactPhone;
    /**
     * 入职时间
     */
    private String entryTime;
    /**
     * 员工类型(1.全职、2.兼职、3.实习、4.劳务派遣、5.退休返聘、6.劳务外包、7.无类型)
     */
    private Integer type;
    /**
     * 转正日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date regularizationDate;
    /**
     * 参与工作日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date participationDate;
    /**
     * 上家单位离职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastDepartureDate;
    /**
     * 隶属公司
     */
    private String subjection;
    /**
     * 部门ID
     */
//    @NotNull(message="部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long deptId;
    /**
     * 部门
     */
    private String dept;
    /**
     * 岗位
     */
    private String post;
    /**
     * 角色ID列表
     */
    @TableField(exist = false)
    private List<Long> roleIdList;
    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<String> roleList;

    /**
     * 状态; 0：正常   1：禁用  2：离职
     */
    private Integer status;
    /**
     * 最高学历
     */
    private String highestEducation;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 毕业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date graduateDate;
    /**
     * 专业
     */
    private String majors;
    /**
     * 绑定IP
     */
    private String ip;
    /**
     * 失败次数
     */
    private Integer defeats;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除
     */
    private Integer delFlag;
    /**
     * 创建者ID
     */
    private Long createUserId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 离职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date resignationTime;
    /**
     * 中介关联用户ID
     */
    private Long belongUserid;
    /**
     * 密码修改次数
     */
    private Integer changeNumber;
    /**
     * 是否建立档案;0不建立,1建立）
     */
    private Integer isBookbuilding;
}
