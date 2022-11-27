package com.awe.service.impl;

import com.awe.mapper.SysUserMapper;
import com.awe.model.entity.SysUserDO;
import com.awe.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户 业务层处理
 * 
 * @author BionGo
 */
@Service
public class SysUserServiceImpl implements SysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUserDO selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }
}
