package com.specimen.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 统计数据DTO
 */
@Data
public class StatisticsDTO {

    private Long totalSpecimens;  // 标本总数
    private Long totalCollections;  // 采集记录总数
    private Long totalUsers;  // 用户总数

    // 按月份统计采集数量
    private List<Map<String, Object>> collectionByMonth;

    // 按科属统计
    private List<Map<String, Object>> specimenByFamily;
    private List<Map<String, Object>> specimenByGenus;
}
