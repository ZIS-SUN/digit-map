package com.specimen.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 标本DTO
 */
@Data
public class SpecimenDTO {

    private Long id;
    private String specimenNo;
    private String name;
    private String latinName;
    private String family;
    private String genus;
    private Long collectorId;
    private String collectorName;  // 采集人姓名
    private LocalDate collectionDate;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String imageUrl;
    private String description;
    private String status;
}
