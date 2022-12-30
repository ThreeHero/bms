import http from '@/utils/service'

/**
 * 新增用户
 * @param {*} data 
 * @returns 
 */
export function saveUser(data) {
  return http({
    url: '/user',
    method: 'POST',
    data
  })
}
/**
 * 删除用户
 * @param {*} id 
 * @returns 
 */
export function removeUser(id) {
  return http({
    url: `/user/${id}`,
    method: 'DELETE'
  })
}
/**
 * 修改用户
 * @param {*} data 
 * @returns 
 */
export function updateUser(data) {
  return http({
    url: '/user',
    method: 'PUT',
    data
  })
}
/**
 * 查询用户
 * @param {*} id 
 * @returns 
 */
export function getUserById(id) {
  return http({
    url: `/user/${id}`,
    method: 'GET'
  })
}
/**
 * 分页查询
 * @param {*} params 
 * @returns 
 */
export function getUserList(params) {
  return http({
    url: '/user/page',
    method: 'GET',
    params
  })
}

// 导出用户数据
export function exp() {
  return http({
    url: '/user/export',
    method: 'GET',
    responseType: 'blob'
  })
}

// 修改密码
export function updatePass(data) {
  return http({
    url: '/user/password',
    method: 'POST',
    data
  })
}