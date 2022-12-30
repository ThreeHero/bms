<template>
  <div class="container">
    <el-upload
      :action="BASE_URL + '/file/upload'"
      :show-file-list="false"
      :on-success="uploadFile"
    >
      <el-button type="primary" @click="">
        上传文件
        <el-icon style="margin-left: 5px;"><Upload /></el-icon>
      </el-button>
    </el-upload>
  </div>
</template>

<script setup>
import emitter from '../utils/myBus.js'
import { ElMessage } from 'element-plus'
import { BASE_URL } from '@/utils/service'

const uploadFile = (response) => {
  if (response.code === 200) {
    // 通知组件重新获取数据
    emitter.emit('againRequest')
  } else {
    ElMessage.error(response.message)
  }
}
</script>

<style scoped lang="less">
.container {
  margin-bottom: 15px;
}
</style>
