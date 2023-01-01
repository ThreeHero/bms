<template>
  <el-aside :width="!isFold ? '200px' : '64px'" class="side">
    <div class="title">
      后台<span class="sys" v-show="!isFold">管理系统</span>
    </div>
    <el-menu
      :default-active="route.path"
      :collapse-transition="false"
      :collapse="isFold"
      router
      v-for="menu in data" :key="menu.id"
    >
      <el-menu-item :index="menu.path" v-if="!menu.pid && menu.path.charAt(0) === '/'">
        <el-icon> <component :is="menu.icon"/> </el-icon>
        <template #title>{{ menu.title }}</template>
      </el-menu-item>
      <el-sub-menu :index="menu.path" v-else>
        <template #title>
          <el-icon><component :is="menu.icon"/></el-icon>
          <span>{{ menu.title }}</span>
        </template>
        <el-menu-item :index="item.path" v-for="item in menu.children">
          <el-icon><component :is="item.icon"/></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
        
      </el-sub-menu>
      
    </el-menu>
  </el-aside>
</template>

<script setup>
import { reactive } from 'vue'
import { useRoute } from 'vue-router'
import { getMenuByRoleId } from '@/api/menu/menu.js'
import { getCache } from '@/utils/storage'
import useToTree from '@/hooks/useToTree'

const props = defineProps({
  isFold: {
    type: Boolean,
    required: true
  }
})
const data = reactive([])

// 请求菜单列表
const getMenuList = async () => {
  const res = await getMenuByRoleId(getCache('userInfo').roleId)
  const result = useToTree(res)
  result.forEach(item => data.push(item))
}
getMenuList()
const route = useRoute()
</script>

<style scoped lang="less">
.side {
  background-color: #fff;
  border-right: 1px solid #ccc;
  overflow-x: hidden;
  box-shadow: 2px 0 6px rgb(255 224 214 / 65%);

  .title {
    height: 60px;
    line-height: 60px;
    text-align: center;
    font-size: 20px;
    user-select: none;
    font-weight: 700;
    color: #333;
    border-bottom: 1px solid #ccc;

    .sys {
      font-weight: 550;
      color: #000;
    }
  }
}
</style>
