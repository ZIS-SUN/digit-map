package com.specimen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.specimen.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告Mapper
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
