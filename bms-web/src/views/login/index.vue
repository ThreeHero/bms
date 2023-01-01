<template>
  <div class="container">
    <div class="login-box">
      <!-- 标题 -->
      <h2 class="title">登 录</h2>
      <!-- 表单区域 -->
      <el-form 
        :model="userInfo" 
        class="from" 
        :rules="rules"
        ref="formRef"
      >
        <el-form-item prop="username">
          <el-input v-model="userInfo.username" placeholder="请输入用户名" @keyup.enter="loginHandle">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="userInfo.password" 
            placeholder="请输入密码" 
            type="password" 
            show-password
            @keyup.enter="loginHandle"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="loginHandle">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/login/login.js'
import { setCache } from '@/utils/storage'
import useLoginStore from '@/stores/login/login.js'
import { getMenuByRoleId } from '@/api/menu/menu.js'
import { getRouter } from '@/router'



const formRef = ref()
const userInfo = reactive({
  username: 'admin',
  password: 'admin'
})
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
})
const router = useRouter() // 路由
const loginStore = useLoginStore() // pinia

const loginHandle = () => {
  formRef.value.validate(async isOK => {
    if (isOK) {
      const res = await login(userInfo)
      if (typeof res !== 'string') {
        // 登录成功
        ElMessage.success("登录成功")
        // 将用户信息存入localStore
        setCache('userInfo', res.user)
        // 将用户信息存入pinia
        loginStore.setUserInfo(res.user)
        // 将token存入localStore和pinia
        setCache('token', res.token)
        loginStore.setToken(res.token)
        // 将路由信息存储到localStore中
        const data = await getMenuByRoleId(res.user.roleId)
        setCache('router', data)
        getRouter(data)
        // 路由跳转至首页
        router.replace('/')
      } else {
        ElMessage.error(res)
        userInfo.password = ''
      }
    }
  })
  
}

</script>

<style scoped lang="less">
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: #ADA996;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to left, #EAEAEA, #DBDBDB, #F2F2F2, #ADA996);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to left, #EAEAEA, #DBDBDB, #F2F2F2, #ADA996); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  overflow: hidden;

  .login-box {
    transform: translateY(-20%);
    width: 350px;
    height: 300px;
    background: rgba(255, 255, 255, .8);
    border-radius: 10px;
    overflow: hidden;

    .title {
      padding: 20px;
      padding-bottom: 10px;
      text-align: center;
      user-select: none;
      margin-top: 12px;
    }

  }
}

/deep/.el-form-item__content {
  padding-top: 15px;
  justify-content: center;

  .el-form-item__error {
    margin-left: 8%;
  }
  
  .el-input {
    width: 90%;
    margin-right: 0;
    
    .el-input__wrapper {
      border-radius: 0;
      box-shadow: none;
      background-color: transparent;
      // padding: 5px;
      border-bottom: 1px solid #ccc;

      .el-input__inner {
        // 字间距
        letter-spacing: 2px;
      }
    }
  }

  .el-button {
    width: 90%;
    height: 40px;
    letter-spacing: 5px;
    background-color: #fff;
    border-radius: 0;
    border: 1px solid #ccc;
    color: #333;
    transition: all .5s;

    &:hover {
      border-color: #000;
      color: #000;
    }
  }
}
</style>
