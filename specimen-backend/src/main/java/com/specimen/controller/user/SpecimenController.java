package com.specimen.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.common.Result;
import com.specimen.dto.SpecimenDTO;
import com.specimen.dto.SpecimenQueryRequest;
import com.specimen.service.SpecimenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端标本控制器
 */
@Tag(name = "标本浏览")
@RestController
@RequestMapping("/api/specimens")
@RequiredArgsConstructor
public class SpecimenController {

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
}
