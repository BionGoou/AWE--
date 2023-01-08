package com.awe.service.impl;

import com.awe.constant.CacheConstant;
import com.awe.core.context.AuthenticationContextHolder;
import com.awe.component.RedisCache;
import com.awe.exception.ServiceException;
import com.awe.mapper.SysUserMapper;
import com.awe.model.entity.SysUserDO;
import com.awe.model.other.LoginUser;
import com.awe.service.SysLoginService;
import com.awe.utils.JwtUtil;
import com.awe.utils.StringUtils;
import com.awe.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public Map<String,String> login(String username, String password, String code, String uuid) {
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
        loginUser.setUuid(UUID);
        redisCache.setCacheObject(CacheConstant.USER_INFO_KEY + UUID, loginUser);
        Map<String,String> returnObj = new HashMap<>();
        if(StringUtils.isBlank(loginUser.getUser().getGender())){
            returnObj.put("isRegisInfo","0");
        }else{
            returnObj.put("isRegisInfo","1");
        }
        returnObj.put("token",JwtUtil.createJWT(UUID,loginUser.getUsername()));
        returnObj.put("role",loginUser.getUser().getRole());
        return returnObj;
    }

    @Transactional
    @Override
    public void doRegister(String username, String password) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserName(username);
        sysUserDO.setPassword(password);
        this.sysUserMapper.doRegister(sysUserDO);
        if(sysUserDO.getUserId() == null){
            throw new ServiceException("注册失败");
        }
        this.sysUserMapper.connectToRoleTable(sysUserDO.getUserId());
    }
}
