<template>
  <el-container class="container">
    <aside-bar :is-fold="isFold" />

    <el-container class="main-box">
      <nav-bar :is-fold="isFold" @change-fold="toggleFold" />
      <main-box />
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { setCache, getCache } from '@/utils/storage'
import AsideBar from './AsideBar'
import NavBar from './NavBar'
import MainBox from './MainBox'

const isFold =  ref(getCache('isFold', false) ?? false) // 是否折叠

const toggleFold = (newIsFold) => {
  isFold.value = newIsFold
  setCache('isFold', isFold.value, false)
}

</script>

<style scoped lang="less">
.container {
  height: 100%;

  .main-box {
    flex-direction: column;
  }
}
</style>
