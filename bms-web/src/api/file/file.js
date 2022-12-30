import http from '@/utils/service'

// 获取 文件列表
export function getFileList(params) {
  return http({
    url: '/file/page',
    method: 'GET',
    params
  })
}

// 下载文件
// export function download(fileUUID) {
//   return http({
//     url: `/file/${fileUUID}`,
//     method: 'GET'
//   })
// }

// 删除文件
export function removeFile(id) {
  return http({
    url: `/file/${id}`,
    method: 'DELETE'
  })
}