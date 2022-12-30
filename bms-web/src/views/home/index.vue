<template>
  <div class="container">
    <echarts-example />
    <echarts-members /> 
  </div>
</template>

<script setup>
import EchartsExample from './components/EchartsExample.vue'
import EchartsMembers from './components/EchartsMembers.vue'
</script>

<style scoped lang="less">
.container {
  height: 100%;
  display: flex;
  justify-content: space-around;
}
</style>
