package com.awe.service;

/**
 * 登录校验方法
 *
 * @author BionGo
 */
public interface SysLoginService {

     String login(String username, String password, String code, String uuid);
}
