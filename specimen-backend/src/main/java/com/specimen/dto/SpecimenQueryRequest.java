package com.specimen.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 标本查询请求DTO
 */
@Data
public class SpecimenQueryRequest {

    private String name;  // 植物名称（模糊查询）
    private String family;  // 科名
    private String genus;  // 属名
    private String location;  // 采集地点（模糊查询）
    private LocalDate startDate;  // 开始日期
    private LocalDate endDate;  // 结束日期
    private String status;  // 状态

    // 分页参数
    private Integer page = 1;
    private Integer size = 10;
}
