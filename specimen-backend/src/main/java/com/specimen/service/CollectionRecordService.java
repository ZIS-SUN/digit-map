package com.specimen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.dto.CollectionRecordDTO;
import com.specimen.entity.CollectionRecord;

/**
 * 采集记录服务接口
 */
public interface CollectionRecordService {

    /**
     * 添加采集记录
     */
    CollectionRecord addRecord(CollectionRecord record);

    /**
     * 查询用户采集记录
     */
    Page<CollectionRecordDTO> getUserRecords(Long userId, Integer page, Integer size);

    /**
     * 查询所有采集记录
     */
    Page<CollectionRecordDTO> getAllRecords(Integer page, Integer size);

    /**
     * 获取用户采集统计数据
     */
    java.util.Map<String, Object> getUserStats(Long userId);
}
