package com.specimen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.specimen.entity.MapMarker;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地图标注Mapper
 */
@Mapper
public interface MapMarkerMapper extends BaseMapper<MapMarker> {
}
