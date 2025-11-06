package com.specimen.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.entity.Announcement;
import com.specimen.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公告控制器
 */
@Tag(name = "公告中心")
@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "查看公告列表")
    @GetMapping
    public Result<Page<Announcement>> getAnnouncements(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Announcement> result = announcementService.getAnnouncements(page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查看公告详情")
    @GetMapping("/{id}")
    public Result<Announcement> getAnnouncementById(@PathVariable Long id) {
        try {
            Announcement announcement = announcementService.getAnnouncementById(id);
            return Result.success(announcement);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
