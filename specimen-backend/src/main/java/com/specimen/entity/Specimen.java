package com.specimen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 植物标本实体类
 */
@Data
@TableName("specimen")
public class Specimen {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String specimenNo;  // 标本编号

    private String name;  // 植物名称

    private String latinName;  // 拉丁名

    private String family;  // 科名

    private String genus;  // 属名

    private Long collectorId;  // 采集人ID

    private LocalDate collectionDate;  // 采集日期

    private String location;  // 采集地点

    private BigDecimal longitude;  // 经度

    private BigDecimal latitude;  // 纬度

    private String imageUrl;  // 图片URL

    private String description;  // 描述

    private String status;  // 状态

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
