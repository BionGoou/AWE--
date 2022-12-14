package com.awe;

import com.awe.model.entity.SysMenuDO;
import com.awe.service.SysMenuService;
import com.awe.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class AweApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Test
    void contextLoads() throws SQLException {

        logger.trace("跟踪");
        logger.debug("调试");
        logger.info("信息");
        logger.warn("警告");
        logger.error("错误");



    }

}
