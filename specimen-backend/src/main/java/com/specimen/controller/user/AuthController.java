package com.specimen.controller.user;

import com.specimen.common.Result;
import com.specimen.dto.LoginRequest;
import com.specimen.dto.RegisterRequest;
import com.specimen.entity.User;
import com.specimen.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户认证控制器
 */
@Tag(name = "用户认证")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = authService.login(request);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
