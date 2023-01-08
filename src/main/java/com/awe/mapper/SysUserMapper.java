package com.awe.mapper;

import com.awe.model.entity.SysUserDO;

/**
 * 用户表 数据层
 * 
 * @author BionGo
 */
public interface SysUserMapper

{
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUserDO selectUserByUserName(String userName);

    String selectGenderByUsername(String userName);

    void doRegister(SysUserDO sysUserDO);

    void connectToRoleTable(Long userId);
}
