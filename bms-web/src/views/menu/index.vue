<template>
  <div>
    <!-- 表格 -->
    <el-table
      :data="menuList"
      row-key="id"
      border
      default-expand-all
    >
      <el-table-column type="index" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="path" label="路径" />
      <el-table-column prop="icon" label="图标">
        <template #default="scope">
          <el-icon>
            <component :is="scope.row.icon" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, toRefs } from 'vue' 
import { getAll } from '@/api/menu/menu'
import useToTree from '@/hooks/useToTree'

const menuList = reactive([])
const getPage = async () => {
  const res = await getAll()
  const data = useToTree(res)
  while (menuList.length) {
    menuList.pop()
  }
  for (const menu of data) {
    menuList.push(menu)
  }
}
getPage()
</script>

<style scoped lang="less">

</style>
