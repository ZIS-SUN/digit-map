package com.specimen.controller.admin;

import com.specimen.common.Result;
import com.specimen.dto.StatisticsDTO;
import com.specimen.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员仪表盘控制器
 */
@Tag(name = "管理员-仪表盘")
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final StatisticsService statisticsService;

    @Operation(summary = "获取统计数据")
    @GetMapping("/stats")
    public Result<StatisticsDTO> getStatistics() {
        try {
            StatisticsDTO stats = statisticsService.getStatistics();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
