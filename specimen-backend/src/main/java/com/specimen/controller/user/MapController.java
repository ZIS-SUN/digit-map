package com.specimen.controller.user;

import com.specimen.common.Result;
import com.specimen.entity.MapMarker;
import com.specimen.service.MapMarkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地图标注控制器
 */
@Tag(name = "地图展示")
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapMarkerService mapMarkerService;

    @Operation(summary = "获取地图标注数据")
    @GetMapping("/markers")
    public Result<List<MapMarker>> getAllMarkers() {
        try {
            List<MapMarker> markers = mapMarkerService.getAllMarkers();
            return Result.success(markers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
