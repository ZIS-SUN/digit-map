package com.specimen.service;

import com.specimen.dto.StatisticsDTO;

/**
 * 统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取统计数据
     */
    StatisticsDTO getStatistics();
}
