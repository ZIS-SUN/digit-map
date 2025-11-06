package com.specimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.entity.Announcement;
import com.specimen.mapper.AnnouncementMapper;
import com.specimen.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 公告服务实现类
 */
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    public Announcement createAnnouncement(Announcement announcement) {
        announcementMapper.insert(announcement);
        return announcement;
    }

    @Override
    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        announcement.setId(id);
        announcementMapper.updateById(announcement);
        return announcement;
    }

    @Override
    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteById(id);
    }

    @Override
    public Page<Announcement> getAnnouncements(Integer page, Integer size) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getCreateTime);
        return announcementMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.selectById(id);
    }
}
