import request from '@/utils/request'

// 获取公告列表
export function getAnnouncements(params) {
  return request({
    url: '/api/announcements',
    method: 'get',
    params
  })
}

// 获取公告详情
export function getAnnouncementDetail(id) {
  return request({
    url: `/api/announcements/${id}`,
    method: 'get'
  })
}

// 添加公告（管理端）
export function addAnnouncement(data) {
  return request({
    url: '/admin/announcements',
    method: 'post',
    data
  })
}

// 更新公告（管理端）
export function updateAnnouncement(id, data) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'put',
    data
  })
}

// 删除公告（管理端）
export function deleteAnnouncement(id) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'delete'
  })
}

