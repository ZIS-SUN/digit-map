package com.specimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.specimen.dto.LoginRequest;
import com.specimen.dto.RegisterRequest;
import com.specimen.entity.User;
import com.specimen.mapper.UserMapper;
import com.specimen.service.AuthService;
import com.specimen.util.JwtUtil;
import com.specimen.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginRequest request) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());

        return result;
    }

    @Override
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setRole("user");
        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }

    @Override
    public Map<String, Object> adminLogin(LoginRequest request) {
        Map<String, Object> result = login(request);

        // 验证是否为管理员
        String role = (String) result.get("role");
        if (!"admin".equals(role)) {
            throw new RuntimeException("权限不足");
        }

        return result;
    }
}
