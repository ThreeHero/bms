<template>
  <div class="not-found" @contextmenu.prevent="">
    <div class="img">
      <img src="@/assets/image/404.png" alt="404" />
    </div>
    <div class="btn">
      <button @click="router.push('/')">返回首页</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<style scoped lang="less">
.not-found {
  width: 100%;
  height: 100%;
  overflow: hidden;
  user-select: none;
  background: #ECE9E6;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #FFFFFF, #ECE9E6);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #FFFFFF, #ECE9E6); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */


  .img {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -65%) scale(0.45);
  }

  .btn {
    opacity: 0.7;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    button {
      background-color: transparent;
      padding: 8px 12px;
      opacity: 0.7;
      cursor: pointer;
      outline: 0;
      border: 0;
      transition: all 0.5s ease;

      &:hover {
        opacity: 1;
        border-bottom: 1px solid #000;
        transform: translateY(-2px);
      }
    }
  }
}
</style>
