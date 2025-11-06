package com.specimen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采集记录实体类
 */
@Data
@TableName("collection_record")
public class CollectionRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String specimenName;

    private LocalDateTime collectionTime;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
