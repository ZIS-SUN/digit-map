package com.specimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.specimen.entity.MapMarker;
import com.specimen.mapper.MapMarkerMapper;
import com.specimen.service.MapMarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地图标注服务实现类
 */
@Service
@RequiredArgsConstructor
public class MapMarkerServiceImpl implements MapMarkerService {

    private final MapMarkerMapper mapMarkerMapper;

    @Override
    public List<MapMarker> getAllMarkers() {
        return mapMarkerMapper.selectList(null);
    }

    @Override
    public MapMarker getMarkerBySpecimenId(Long specimenId) {
        LambdaQueryWrapper<MapMarker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MapMarker::getSpecimenId, specimenId);
        return mapMarkerMapper.selectOne(wrapper);
    }

    @Override
    public MapMarker updateMarker(Long id, MapMarker marker) {
        MapMarker existingMarker = mapMarkerMapper.selectById(id);
        if (existingMarker == null) {
            throw new RuntimeException("地图标注不存在");
        }
        marker.setId(id);
        mapMarkerMapper.updateById(marker);
        return marker;
    }

    @Override
    public void deleteMarker(Long id) {
        MapMarker marker = mapMarkerMapper.selectById(id);
        if (marker == null) {
            throw new RuntimeException("地图标注不存在");
        }
        mapMarkerMapper.deleteById(id);
    }

    @Override
    public MapMarker getMarkerById(Long id) {
        MapMarker marker = mapMarkerMapper.selectById(id);
        if (marker == null) {
            throw new RuntimeException("地图标注不存在");
        }
        return marker;
    }
}
