<template>
  <el-card class="container" shadow="hover">
    <div ref="echartRef" class="container" />
  </el-card>
  
</template>

<script setup>
import { ref, inject, onMounted } from 'vue'
import option from '../config/EchartsExampleConfig.js'
import { getMembers } from '@/api/echarts/echarts.js'

const echart = inject('echart')
const echartRef = ref(null)

onMounted(async () => {
  const myChart = echart.init(echartRef.value)

  const res = await getMembers()
  option.xAxis.data = res.x
  option.series[0].data = res.data

  myChart.setOption(option)
})
</script>

<style scoped lang="less">
.container {
  padding: 15px;
  width: 40vw;
  height: 380px!important;
}
</style>
