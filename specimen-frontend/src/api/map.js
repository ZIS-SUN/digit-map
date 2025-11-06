import request from '@/utils/request'

// 获取地图标注点列表
export function getMapMarkers(params) {
  return request({
    url: '/api/map/markers',
    method: 'get',
    params
  })
}

// 添加地图标注（管理端）
export function addMapMarker(data) {
  return request({
    url: '/admin/map/markers',
    method: 'post',
    data
  })
}

// 更新地图标注（管理端）
export function updateMapMarker(id, data) {
  return request({
    url: `/admin/map/markers/${id}`,
    method: 'put',
    data
  })
}

// 删除地图标注（管理端）
export function deleteMapMarker(id) {
  return request({
    url: `/admin/map/markers/${id}`,
    method: 'delete'
  })
}

