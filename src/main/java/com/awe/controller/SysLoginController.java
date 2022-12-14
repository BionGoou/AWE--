package com.awe.controller;

import com.awe.constant.Constants;
import com.awe.model.other.AjaxResult;
import com.awe.model.vo.LoginVO;
import com.awe.service.SysLoginService;
import com.awe.service.SysMenuService;
import com.awe.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 *
 * @author BionGo
 */
@RestController
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
        String token = loginService.login(loginVO.getUsername(), loginVO.getPassword(), loginVO.getCode(),
                loginVO.getUuid());
        ajax.put(Constants.TOKEN, token);
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
