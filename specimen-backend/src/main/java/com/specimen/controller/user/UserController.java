package com.specimen.controller.user;

import com.specimen.common.Result;
import com.specimen.common.UserContext;
import com.specimen.entity.User;
import com.specimen.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 */
@Tag(name = "个人信息")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<User> getProfile() {
        try {
            Long userId = UserContext.getUserId();
            User user = userMapper.selectById(userId);
            // 清除密码
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
