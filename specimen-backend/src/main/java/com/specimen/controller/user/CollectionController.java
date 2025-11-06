package com.specimen.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.common.UserContext;
import com.specimen.dto.CollectionRecordDTO;
import com.specimen.entity.CollectionRecord;
import com.specimen.service.CollectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 采集记录控制器
 */
@Tag(name = "采集记录")
@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionRecordService collectionRecordService;

    @Operation(summary = "提交采集记录")
    @PostMapping
    public Result<CollectionRecord> addRecord(@RequestBody CollectionRecord record) {
        try {
            Long userId = UserContext.getUserId();
            record.setUserId(userId);
            CollectionRecord result = collectionRecordService.addRecord(record);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查看我的采集记录")
    @GetMapping("/my")
    public Result<Page<CollectionRecordDTO>> getMyRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = UserContext.getUserId();
            Page<CollectionRecordDTO> result = collectionRecordService.getUserRecords(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查看我的采集统计")
    @GetMapping("/stats")
    public Result<java.util.Map<String, Object>> getMyStats() {
        try {
            Long userId = UserContext.getUserId();
            java.util.Map<String, Object> stats = collectionRecordService.getUserStats(userId);
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
