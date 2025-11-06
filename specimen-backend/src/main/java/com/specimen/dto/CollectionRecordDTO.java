package com.specimen.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采集记录DTO
 */
@Data
public class CollectionRecordDTO {

    private Long id;
    private Long userId;
    private String username;
    private String specimenName;
    private LocalDateTime collectionTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String remarks;
}
