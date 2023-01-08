package com.awe.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 */
@Data
public class SysUserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;


    private String role;

    /**
     * 密码
     */
    private String password;

    /**
     * 关联表中字段
     */
    private String gender;

    /**
     * 活动番号
     */
    private String eventNm;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
