package com.specimen.service.impl;

import com.specimen.dto.StatisticsDTO;
import com.specimen.mapper.CollectionRecordMapper;
import com.specimen.mapper.SpecimenMapper;
import com.specimen.mapper.UserMapper;
import com.specimen.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 统计服务实现类
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final SpecimenMapper specimenMapper;
    private final CollectionRecordMapper collectionRecordMapper;
    private final UserMapper userMapper;

    @Override
    public StatisticsDTO getStatistics() {
        StatisticsDTO dto = new StatisticsDTO();

        // 总数统计
        dto.setTotalSpecimens(specimenMapper.selectCount(null));
        dto.setTotalCollections(collectionRecordMapper.selectCount(null));
        dto.setTotalUsers(userMapper.selectCount(null));

        // 按月份统计采集数量
        dto.setCollectionByMonth(collectionRecordMapper.countByMonth());

        // 按科属统计
        dto.setSpecimenByFamily(specimenMapper.countByFamily());
        dto.setSpecimenByGenus(specimenMapper.countByGenus());

        return dto;
    }
}
