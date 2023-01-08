package com.awe.controller;

import com.awe.constant.Constants;
import com.awe.model.other.AjaxResult;
import com.awe.model.vo.LoginVO;
import com.awe.service.SysLoginService;
import com.awe.service.SysMenuService;
import com.awe.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录验证
 *
 * @author BionGo
 */
@RestController
@RequestMapping("/user")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录方法
     *
     * @param loginVO 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginVO loginVO) {
        AjaxResult ajax = AjaxResult.success();
        Map<String, String> info = loginService.login(loginVO.getUsername(), loginVO.getPassword(), loginVO.getCode(),
                loginVO.getUuid());
        ajax.put(Constants.TOKEN, info.get("token"));
        ajax.put("isRegisInfo", info.get("isRegisInfo"));
        ajax.put("role", info.get("role"));
        return ajax;
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody @Validated LoginVO loginVO) {
        AjaxResult ajax = AjaxResult.success();
        loginService.doRegister(loginVO.getUsername(), new BCryptPasswordEncoder().encode(loginVO.getPassword()));
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        return AjaxResult.success(sysMenuService.selectMenuTreeByUserId(userId));
    }
}
