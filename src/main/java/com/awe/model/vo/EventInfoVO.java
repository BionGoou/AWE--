package com.awe.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class EventInfoVO {

    @NotBlank
    private String eventNum;

    @NotBlank
    private String realName;

    @NotBlank
    private String annualIncome;

    @NotBlank
    private Date birthday;

    @NotBlank
    private String birthplace;

    @NotBlank
    private String finalEducation;

    @NotBlank
    private String visaType;

    @NotBlank
    private String wechatNum;

    @NotBlank
    private String gender;

    @NotBlank
    private String height;

    @NotBlank
    private String maritalStatus;

    private String nuisance;

    @NotBlank
    private String occupation;

    @NotBlank
    private String phoneNum;

    @NotBlank
    private String isPrivate;

    @NotBlank
    private String residence;

    @NotBlank
    private String selfIntroduction;

    @NotBlank
    private String requirement;
}
