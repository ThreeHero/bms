<template>
  <div>
    <el-input placeholder="名称" v-model="name">
      <template #suffix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
    <el-button plain @click="roleSearch">搜 索</el-button>
    <el-button plain type="danger" @click="reset">重 置</el-button>
  </div>
  <div style="margin: 10px 0">
    <el-button type="primary" @click="saveRole">
      <el-icon style="margin-right: 5px"><CirclePlusFilled /></el-icon>
      新 增
    </el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emits = defineEmits(['roleButton', 'saveRole'])
const name = ref('')
const roleSearch = () => {
  emits('roleButton', name.value)
}
const reset = () => {
  name.value = ''
  emits('roleButton')
}
const saveRole = () => {
  emits('saveRole')
}
</script>

<style scoped lang="less">

</style>
