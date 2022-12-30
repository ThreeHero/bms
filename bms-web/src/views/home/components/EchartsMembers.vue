<template>
  <el-card class="container" shadow="hover">
    <div ref="echartRef" class="container" />
  </el-card>
  
</template>

<script setup>
import { ref, inject, onMounted } from 'vue'
import option from '../config/EchartsMembersConfig.js'
import { getMembers } from '@/api/echarts/echarts.js'

const echart = inject('echart')
const echartRef = ref(null)

onMounted(async () => {
  const myChart = echart.init(echartRef.value)

  const res = await getMembers()
  const data = []
  for (let i = 0; i < res.data.length; i++) {
    if (data[i] === undefined) {
      data[i] = {}
    }
    data[i].value = res.data[i]
    data[i].name = res.x[i]
  }
  option.series[0].data = data

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
