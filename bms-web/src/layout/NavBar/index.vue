<template>
  <el-header class="nav">
    <!-- 图标 -->
    <div class="icon">
      <el-icon size="22" @click="toggleFold">
        <Fold v-if="!isFold" />
        <Expand v-else />
      </el-icon>
    </div>
    <!-- 下拉框 -->
    <el-dropdown>
      <span style="cursor: pointer">
        <el-avatar :size="25" :src="loginStore.avatarUrl" style="vertical-align: middle" />
        {{ loginStore.userInfo?.nickname }}
        <el-icon>
          <arrow-down />
        </el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="router.push('/person')">个人中心</el-dropdown-item>
          <el-dropdown-item divided @click="layoutHandle">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import useLoginStore from '@/stores/login/login'
import { removeCache } from '@/utils/storage'

const router = useRouter()
const loginStore = useLoginStore()
const props = defineProps({
  isFold: {
    type: Boolean,
    required: true
  }
})
const emits = defineEmits(['changeFold'])
// 修改折叠状态
const toggleFold = () => {
  // 通知父组件修改折叠状态
  emits('changeFold', !props.isFold)
}
// 退出登录
const layoutHandle = () => {
  // 清除pinia 中的用户信息
  loginStore.setUserInfo('')
  // 清除localStore
  removeCache('userInfo')
  // 清楚token
  loginStore.setToken('')
  removeCache('token')
  // 跳转至登录页
  router.replace('/login')
}
</script>

<style scoped lang="less">  
.nav {
  display: flex;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  justify-content: space-between;
  align-items: center;

  .icon {
    cursor: pointer;
  }
}
</style>
