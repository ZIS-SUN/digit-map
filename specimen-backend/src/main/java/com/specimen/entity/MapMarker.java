package com.specimen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 地图标注实体类
 */
@Data
@TableName("map_marker")
public class MapMarker {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long specimenId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String label;

    private String markerColor;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
