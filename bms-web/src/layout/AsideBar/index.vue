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
    >
      <el-menu-item index="/home">
        <el-icon><House></House></el-icon>
        <template #title>首页</template>
      </el-menu-item>
      <el-sub-menu index="sys">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
        <el-menu-item index="/role">
          <el-icon><UserFilled /></el-icon>
          <template #title>角色管理</template>
        </el-menu-item>
         <el-menu-item index="/file">
          <el-icon><Document /></el-icon>
          <template #title>文件管理</template>
        </el-menu-item>
      </el-sub-menu>
      
    </el-menu>
  </el-aside>
</template>

<script setup>
import { useRoute } from 'vue-router'
const props = defineProps({
  isFold: {
    type: Boolean,
    required: true
  }
})

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
