import http from '@/utils/service'

export function getMenuById(id) {
  return http({
    url: `/menu/${id}`,
    method: 'GET'
  })
}

export function getMenuByRoleId(roleId) {
  return http({
    url: `/menu/role/${roleId}`,
    method: 'GET'
  })
}

export function getAll() {
  return http({
    url: '/menu',
    method: 'GET'
  }) 
}