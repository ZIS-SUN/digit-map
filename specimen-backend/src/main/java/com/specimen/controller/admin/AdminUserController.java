package com.specimen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.entity.User;
import com.specimen.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户管理控制器
 */
@Tag(name = "管理员-用户管理")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserMapper userMapper;

    @Operation(summary = "查询用户列表")
    @GetMapping
    public Result<Page<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            if (username != null && !username.isEmpty()) {
                wrapper.like(User::getUsername, username);
            }
            Page<User> result = userMapper.selectPage(new Page<>(page, size), wrapper);

            // 清除密码
            result.getRecords().forEach(user -> user.setPassword(null));

            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            User user = new User();
            user.setId(id);
            user.setStatus(status);
            userMapper.updateById(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
