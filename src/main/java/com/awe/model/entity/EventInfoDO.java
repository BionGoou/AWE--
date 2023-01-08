package com.awe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class EventInfoDO implements Serializable {

    @TableId
    private Long primaryKey;

    private String eventNum;

    @TableField("real_name")
    private String realName;

    private String annualIncome;

    private Date birthday;

    private String birthplace;

    @TableField("final_education")
    private String finalEducation;

    private String visaType;

    private String wechatNum;

    private String gender;

    private String height;

    private String maritalStatus;

    private String nuisance;

    private String occupation;

    private String phoneNum;

    @TableField("private_flag")
    private String isPrivate;

    private String profilePhoto;

    private String residence;

    private String selfIntroduction;

    private String requirement;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private Date updateTime;
}
