package com.specimen.controller.admin;

import com.specimen.common.Result;
import com.specimen.dto.LoginRequest;
import com.specimen.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员认证控制器
 */
@Tag(name = "管理员认证")
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> adminLogin(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = authService.adminLogin(request);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
