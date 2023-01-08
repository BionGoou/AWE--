package com.awe;

import com.awe.mapper.EventMapper;
import com.awe.mapper.SysUserMapper;
import com.awe.model.entity.EventInfoDO;
import com.awe.model.entity.SysUserDO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class AweApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysUserMapper sysUserMapper;


    @Test
    void contextLoads() throws SQLException {

        String hahaha = sysUserMapper.selectGenderByUsername("admin");
        System.out.println(hahaha);
    }

}
