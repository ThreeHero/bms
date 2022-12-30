import http from '@/utils/service'

/**
 * 用户登录
 * @param {*} data 
 * @returns 
 */
export function login(data) {
  return http({
    url: '/user/login',
    method: 'POST',
    data
  })
}