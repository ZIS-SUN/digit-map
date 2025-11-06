package com.specimen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.specimen.entity.Announcement;

/**
 * 公告服务接口
 */
public interface AnnouncementService {

    /**
     * 创建公告
     */
    Announcement createAnnouncement(Announcement announcement);

    /**
     * 更新公告
     */
    Announcement updateAnnouncement(Long id, Announcement announcement);

    /**
     * 删除公告
     */
    void deleteAnnouncement(Long id);

    /**
     * 获取公告列表
     */
    Page<Announcement> getAnnouncements(Integer page, Integer size);

    /**
     * 获取公告详情
     */
    Announcement getAnnouncementById(Long id);
}
