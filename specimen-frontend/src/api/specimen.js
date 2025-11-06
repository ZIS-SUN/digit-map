import request from '@/utils/request'

// 获取标本列表
export function getSpecimens(params) {
  return request({
    url: '/api/specimens',
    method: 'get',
    params
  })
}

// 获取标本详情
export function getSpecimenDetail(id) {
  return request({
    url: `/api/specimens/${id}`,
    method: 'get'
  })
}

// 添加标本（管理端）
export function addSpecimen(data) {
  return request({
    url: '/admin/specimens',
    method: 'post',
    data
  })
}

// 更新标本（管理端）
export function updateSpecimen(id, data) {
  return request({
    url: `/admin/specimens/${id}`,
    method: 'put',
    data
  })
}

// 删除标本（管理端）
export function deleteSpecimen(id) {
  return request({
    url: `/admin/specimens/${id}`,
    method: 'delete'
  })
}

// 导出标本数据（管理端）
export function exportSpecimens() {
  return request({
    url: '/admin/specimens/export',
    method: 'get',
    responseType: 'blob'
  })
}

