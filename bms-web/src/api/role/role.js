import http from '@/utils/service'


/**
 * 新增角色
 * @param {*} data 
 * @returns 
 */
export function saveRole(data) {
  return http({
    url: '/role',
    method: 'POST',
    data
  })
}

/**
 * 删除角色
 * @param {*} id 
 * @returns 
 */
export function removeRole(id) {
  return http({
    url: `/role/${id}`,
    method: 'DELETE',
  })
}

/**
 * 修改角色
 * @param {*} data 
 * @returns 
 */
export function updateRole(data) {
  return http({
    url: '/role',
    method: 'PUT',
    data
  })
}

/**
 * 根据id查询角色
 * @param {*} id 
 * @returns 
 */
export function getRoleById(id) {
  return http({
    url: `/role/${id}`,
    method: 'GET'
  })
}

export function getRoleList(params) {
  return http({
    url: '/role/page',
    method: 'GET',
    params
  })
}