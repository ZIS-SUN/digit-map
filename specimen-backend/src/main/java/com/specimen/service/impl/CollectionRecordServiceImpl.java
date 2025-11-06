package com.specimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.dto.CollectionRecordDTO;
import com.specimen.entity.CollectionRecord;
import com.specimen.entity.User;
import com.specimen.mapper.CollectionRecordMapper;
import com.specimen.mapper.UserMapper;
import com.specimen.service.CollectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采集记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class CollectionRecordServiceImpl implements CollectionRecordService {

    private final CollectionRecordMapper collectionRecordMapper;
    private final UserMapper userMapper;

    @Override
    public CollectionRecord addRecord(CollectionRecord record) {
        collectionRecordMapper.insert(record);
        return record;
    }

    @Override
    public Page<CollectionRecordDTO> getUserRecords(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<CollectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectionRecord::getUserId, userId);
        wrapper.orderByDesc(CollectionRecord::getCollectionTime);

        Page<CollectionRecord> pageResult = collectionRecordMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToDTO(pageResult);
    }

    @Override
    public Page<CollectionRecordDTO> getAllRecords(Integer page, Integer size) {
        LambdaQueryWrapper<CollectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CollectionRecord::getCollectionTime);

        Page<CollectionRecord> pageResult = collectionRecordMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToDTO(pageResult);
    }

    @Override
    public Map<String, Object> getUserStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // 用户总采集次��
        LambdaQueryWrapper<CollectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectionRecord::getUserId, userId);
        Long totalCollections = collectionRecordMapper.selectCount(wrapper);
        stats.put("totalCollections", totalCollections);

        // 按月份统计
        List<Map<String, Object>> collectionByMonth = collectionRecordMapper.countByMonthForUser(userId);
        stats.put("collectionByMonth", collectionByMonth);

        return stats;
    }

    private Page<CollectionRecordDTO> convertToDTO(Page<CollectionRecord> page) {
        Page<CollectionRecordDTO> dtoPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<CollectionRecordDTO> dtoList = page.getRecords().stream()
                .map(record -> {
                    CollectionRecordDTO dto = new CollectionRecordDTO();
                    BeanUtils.copyProperties(record, dto);

                    // 获取用户名
                    User user = userMapper.selectById(record.getUserId());
                    if (user != null) {
                        dto.setUsername(user.getUsername());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }
}
