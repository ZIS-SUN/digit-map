package com.specimen.service;

import com.specimen.entity.MapMarker;

import java.util.List;

/**
 * 地图标注服务接口
 */
public interface MapMarkerService {

    /**
     * 获取所有地图标注
     */
    List<MapMarker> getAllMarkers();

    /**
     * 根据标本ID获取标注
     */
    MapMarker getMarkerBySpecimenId(Long specimenId);

    /**
     * 更新地图标注
     */
    MapMarker updateMarker(Long id, MapMarker marker);

    /**
     * 删除地图标注
     */
    void deleteMarker(Long id);

    /**
     * 根据标注ID获取标注
     */
    MapMarker getMarkerById(Long id);
}
