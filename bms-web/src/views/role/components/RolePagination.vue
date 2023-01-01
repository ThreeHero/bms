<template>
  <el-pagination 
    layout="sizes, prev, pager, next, total"
      :page-sizes="[5, 10, 15, 20]"
      :total="total"
      :default-page-size="pageSize"
      :current-page="page"
      @current-change="changeCurrentPage"
      @size-change="changePageSize"
  />
</template>

<script setup>
const props = defineProps(['page', 'pageSize', 'total'])
const emits = defineEmits(['changeCurrentPage', 'changePageSize'])


const changeCurrentPage = (currentPage) => {
  emits('changeCurrentPage', currentPage)
}
const changePageSize = (pageS) => {
  emits('changePageSize', pageS)
}
</script>

<style scoped lang="less">

</style>
