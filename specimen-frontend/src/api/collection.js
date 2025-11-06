import request from '@/utils/request'

// 获取采集记录列表
export function getCollections(params) {
  return request({
    url: '/api/collections',
    method: 'get',
    params
  })
}

// 添加采集记录
export function addCollection(data) {
  return request({
    url: '/api/collections',
    method: 'post',
    data
  })
}

// 获取采集统计数据
export function getCollectionStats() {
  return request({
    url: '/api/collections/stats',
    method: 'get'
  })
}

// 获取所有采集记录（管理端）
export function getAllCollections(params) {
  return request({
    url: '/api/collections/all',
    method: 'get',
    params
  })
}

// 删除采集记录（管理端）
export function deleteCollection(id) {
  return request({
    url: `/admin/collections/${id}`,
    method: 'delete'
  })
}

