package com.specimen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.dto.CollectionRecordDTO;
import com.specimen.service.CollectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员采集记录控制器
 */
@Tag(name = "管理员-采集记录管理")
@RestController
@RequestMapping("/admin/collections")
@RequiredArgsConstructor
public class AdminCollectionController {

    private final CollectionRecordService collectionRecordService;

    @Operation(summary = "查询所有采集记录")
    @GetMapping
    public Result<Page<CollectionRecordDTO>> getAllRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<CollectionRecordDTO> result = collectionRecordService.getAllRecords(page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查询指定用户的采集记录")
    @GetMapping("/user/{userId}")
    public Result<Page<CollectionRecordDTO>> getUserRecords(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<CollectionRecordDTO> result = collectionRecordService.getUserRecords(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
