import axios from 'axios'
import { getCache } from '../storage'

// const BASE_URL = 'http://127.0.0.1:9000'
export const BASE_URL = 'http://192.168.1.4:9000'
const TIME_OUT = 5000 // 5s后超时


const service = axios.create({
  baseURL: BASE_URL,
  timeout: TIME_OUT
})

// 请求拦截器
service.interceptors.request.use(config => {
  // 添加请求头
  config.headers['Content-Type'] = 'application/json;charset=utf-8'

  if (getCache('token')) {
    // 携带token
    config.headers['Authorization'] = `Bearer ${getCache('token')}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(response => {
  const { code, message, data } = response.data
  // 状态码为200为成功
  if (code === 200) {
    return data
  } else {
    return message
  }
}, error => {
  return Promise.reject(error)
})

export default service