package com.specimen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.specimen.entity.CollectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 采集记录Mapper
 */
@Mapper
public interface CollectionRecordMapper extends BaseMapper<CollectionRecord> {

    /**
     * 按月份统计采集数量
     */
    @Select("SELECT DATE_FORMAT(collection_time, '%Y-%m') as month, COUNT(*) as count " +
            "FROM collection_record " +
            "GROUP BY month " +
            "ORDER BY month DESC " +
            "LIMIT 12")
    List<Map<String, Object>> countByMonth();

    /**
     * 按月份统计用户采集数量
     */
    @Select("SELECT DATE_FORMAT(collection_time, '%Y-%m') as month, COUNT(*) as count " +
            "FROM collection_record " +
            "WHERE user_id = #{userId} " +
            "GROUP BY month " +
            "ORDER BY month DESC " +
            "LIMIT 12")
    List<Map<String, Object>> countByMonthForUser(Long userId);
}
