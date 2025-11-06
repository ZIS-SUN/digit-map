package com.specimen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.dto.SpecimenDTO;
import com.specimen.dto.SpecimenQueryRequest;
import com.specimen.entity.Specimen;
import com.specimen.service.SpecimenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理员标本控制器
 */
@Tag(name = "管理员-标本管理")
@RestController
@RequestMapping("/admin/specimens")
@RequiredArgsConstructor
public class AdminSpecimenController {

    private final SpecimenService specimenService;

    @Operation(summary = "查询标本列表")
    @GetMapping
    public Result<Page<SpecimenDTO>> querySpecimens(SpecimenQueryRequest request) {
        try {
            Page<SpecimenDTO> page = specimenService.querySpecimens(request);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查看标本详情")
    @GetMapping("/{id}")
    public Result<SpecimenDTO> getSpecimenById(@PathVariable Long id) {
        try {
            SpecimenDTO specimen = specimenService.getSpecimenById(id);
            return Result.success(specimen);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "添加标本")
    @PostMapping
    public Result<Specimen> addSpecimen(
            @RequestPart("specimen") Specimen specimen,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Specimen result = specimenService.addSpecimen(specimen, imageFile);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "修改标本")
    @PutMapping("/{id}")
    public Result<Specimen> updateSpecimen(
            @PathVariable Long id,
            @RequestPart("specimen") Specimen specimen,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Specimen result = specimenService.updateSpecimen(id, specimen, imageFile);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "删除标本")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSpecimen(@PathVariable Long id) {
        try {
            specimenService.deleteSpecimen(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "导出标本数据")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportSpecimens(SpecimenQueryRequest request) {
        try {
            byte[] data = specimenService.exportSpecimens(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "specimens.csv");
            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
