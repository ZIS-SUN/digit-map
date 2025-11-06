package com.specimen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.specimen.entity.Specimen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 标本Mapper
 */
@Mapper
public interface SpecimenMapper extends BaseMapper<Specimen> {

    /**
     * 按科属统计
     */
    @Select("SELECT family, COUNT(*) as count FROM specimen WHERE family IS NOT NULL GROUP BY family ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> countByFamily();

    @Select("SELECT genus, COUNT(*) as count FROM specimen WHERE genus IS NOT NULL GROUP BY genus ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> countByGenus();
}
