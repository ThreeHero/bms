<template>
  <el-pagination
    class="pagination"
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
import { ref, onBeforeUnmount } from 'vue'
import emitter from '../utils/myBus.js'

const total = ref(0)
const page = ref(1) // 当前页数
const pageSize = ref(5) // 每页条数

const changeCurrentPage = (currentPage) => {
  page.value = currentPage
  emitter.emit('changePage', page.value)
}
const changePageSize = (pageS) => {
  pageSize.value = pageS
  emitter.emit('changePageSize', pageSize.value)
}

emitter.on('changeTotal', (newTotal) => {
  total.value = newTotal
})

onBeforeUnmount(() => {
  emitter.off('changeTotal')
})


</script>

<style scoped lang="less">

</style>
