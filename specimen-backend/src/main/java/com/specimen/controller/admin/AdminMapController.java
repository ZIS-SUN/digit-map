package com.specimen.controller.admin;

import com.specimen.common.Result;
import com.specimen.entity.MapMarker;
import com.specimen.service.MapMarkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员地图管理控制器
 */
@Tag(name = "管理员-地图管理")
@RestController
@RequestMapping("/admin/map")
@RequiredArgsConstructor
public class AdminMapController {

    private final MapMarkerService mapMarkerService;

    @Operation(summary = "查看全部标注")
    @GetMapping("/markers")
    public Result<List<MapMarker>> getAllMarkers() {
        try {
            List<MapMarker> markers = mapMarkerService.getAllMarkers();
            return Result.success(markers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查看标注详情")
    @GetMapping("/markers/{id}")
    public Result<MapMarker> getMarkerById(@PathVariable Long id) {
        try {
            MapMarker marker = mapMarkerService.getMarkerById(id);
            return Result.success(marker);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新地图标注")
    @PutMapping("/markers/{id}")
    public Result<MapMarker> updateMarker(
            @PathVariable Long id,
            @RequestBody MapMarker marker) {
        try {
            MapMarker result = mapMarkerService.updateMarker(id, marker);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "删除地图标注")
    @DeleteMapping("/markers/{id}")
    public Result<Void> deleteMarker(@PathVariable Long id) {
        try {
            mapMarkerService.deleteMarker(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
