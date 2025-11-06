package com.specimen.service;

import com.specimen.dto.LoginRequest;
import com.specimen.dto.RegisterRequest;
import com.specimen.entity.User;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    Map<String, Object> login(LoginRequest request);

    /**
     * 用户注册
     */
    User register(RegisterRequest request);

    /**
     * 管理员登录
     */
    Map<String, Object> adminLogin(LoginRequest request);
}
