import http from '@/utils/service'

export function getExample() {
  return http({
    url: '/echarts/example',
    method: 'GET'
  })
}

export function getMembers() {
  return http({
    url: '/echarts/members',
    method: 'GET'
  })
}