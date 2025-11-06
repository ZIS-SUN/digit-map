package com.specimen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.entity.Announcement;
import com.specimen.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员公告管理控制器
 */
@Tag(name = "管理员-公告管理")
@RestController
@RequestMapping("/admin/announcements")
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "查询公告列表")
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

    @Operation(summary = "发布公告")
    @PostMapping
    public Result<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        try {
            Announcement result = announcementService.createAnnouncement(announcement);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "修改公告")
    @PutMapping("/{id}")
    public Result<Announcement> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody Announcement announcement) {
        try {
            Announcement result = announcementService.updateAnnouncement(id, announcement);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        try {
            announcementService.deleteAnnouncement(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
