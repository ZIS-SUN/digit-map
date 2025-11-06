import request from '@/utils/request'

// 获取用户列表（管理端）
export function getUsers(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

// 更新用户状态（管理端）
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/users/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 删除用户（管理端）
export function deleteUser(id) {
  return request({
    url: `/admin/users/${id}`,
    method: 'delete'
  })
}

