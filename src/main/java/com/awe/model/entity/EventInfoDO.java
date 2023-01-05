package com.awe.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class EventInfoDO implements Serializable {

    @TableId
    private String primaryKey;

    private String eventNum;

    private String realName;

    private String annualIncome;

    private Date birthday;

    private String birthplace;

    private String finalEducation;

    private String visaType;

    private String wechatNum;

    private String gender;

    private String height;

    private String maritalStatus;

    private String nuisance;

    private String occupation;

    private String phoneNum;

    private String isPrivate;

    private String residence;

    private String selfIntroduction;

    private String requirement;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
