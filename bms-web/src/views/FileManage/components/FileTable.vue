<template>
  <el-table :data="fileList" border>
    <el-table-column type="index" align="center" />
    <el-table-column prop="name" label="文件名称" />
    <el-table-column prop="type" label="文件类型" />
    <el-table-column prop="size" label="文件大小(Kb)" />
    <el-table-column prop="date" label="下载">
      <template #default="scope">
        <el-button type="primary" @click="downloadFile(scope.row)">下载</el-button>
      </template>
    </el-table-column>
    <el-table-column prop="date" label="操作">
      <template #default="scope">
        <el-button type="danger" @click="deleteFile(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import emitter from '../utils/myBus.js'
import { getFileList, removeFile } from '@/api/file/file.js'
import { BASE_URL } from '@/utils/service'


const fileList = reactive([]) // 文件列表
const page = ref(1) // 当前页数
const pageSize = ref(5) // 每页条数

// 获取文件列表
const getPage = async () => {
  const res = await getFileList({
    page: page.value,
    pageSize: pageSize.value
  })

  emitter.emit('changeTotal', res.total)

  // 清空
  while (fileList.length) {
    fileList.pop()
  }

  for (const file of res.records) {
    fileList.push(file)
  }
}

getPage()

// 下载文件
const downloadFile = ({ url }) => {
  window.open(`${BASE_URL}/file/${url.split('/').at(-1)}`)
  ElMessage.success('下载成功')
}

// 删除文件
const deleteFile = (file) => {
  ElMessageBox.confirm(
    '删除文件后无法恢复，请确认!',
    '确认删除',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(async () => {
    // 删除当前行并重新请求数据
    await removeFile(file.id)
    getPage()
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
  })
  .catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}

// 监听上传事件
emitter.on('againRequest', getPage)
emitter.on('changePage', (pageNum) => {
  page.value = pageNum
  getPage()
})
emitter.on('changePageSize', (pageS) => {
  pageSize.value = pageS
  getPage()
})

onBeforeUnmount(() => {
  emitter.off('againRequest')
  emitter.off('changePage')
  emitter.off('changePageSize')
})
</script>

<style scoped lang="less">

</style>
