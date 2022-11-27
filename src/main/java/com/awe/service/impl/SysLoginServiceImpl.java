package com.awe.service.impl;

import com.awe.constant.CacheConstant;
import com.awe.context.AuthenticationContextHolder;
import com.awe.core.component.RedisCache;
import com.awe.model.other.LoginUser;
import com.awe.service.SysLoginService;
import com.awe.utils.JwtUtil;
import com.awe.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 登录校验方法
 *
 * @author BionGo
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) throw new RuntimeException("密码不对");
            throw new RuntimeException(e.getMessage());
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String UUID = IdUtils.fastUUID();
        redisCache.setCacheObject(CacheConstant.USER_INFO_KEY + UUID, loginUser);
        return JwtUtil.createJWT(UUID,loginUser.getUsername());
    }
}
