<template>
  <div class="container">
    <!-- 左边 个人信息 -->
    <el-card class="user-info" shadow="hover">
      <el-descriptions title="个人中心" border :column="1">
        <template #extra>
          <el-button type="primary" @click="changeEditState">{{ isEdit? '完成': '编辑' }}</el-button>
        </template>
        <el-descriptions-item label="用户名">
          <el-input v-model="user.username" :disabled="true"></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="头像">
          <el-upload
            :action="BASE_URL + '/file/upload'"
            :show-file-list="false"
            :on-success="uploadAvatarFile"
            :disabled="!isEdit"
          >
            <el-avatar 
              v-if="user.avatarUrl" 
              :src="user.avatarUrl" 
              shape="square"
              :size="50"
            />
            <el-icon v-else><Plus /></el-icon>
          </el-upload>
        </el-descriptions-item>
        <el-descriptions-item label="昵称">
          <el-input v-model="user.nickname" :disabled="!isEdit"></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          <el-input v-model="user.email" :disabled="!isEdit"></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="手机">
          <el-input v-model="user.phone" :disabled="!isEdit"></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="地址">
          <el-input v-model="user.address" :disabled="!isEdit"></el-input>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 右边 重置密码 -->
    <el-card class="reset-password" shadow="hover">
      <div class="title">重置密码</div>
      <el-form :rules="rules" ref="passwordRef" :model="passwordData">
       <el-form-item prop="password">
        <el-input 
          type="password"
          show-password
          placeholder="请输入原密码" 
          v-model="passwordData.password"
        />
       </el-form-item>
       <el-form-item prop="newPassword">
        <el-input 
          type="password"
          show-password
          placeholder="请输入新密码" 
          v-model="passwordData.newPassword"
        />
       </el-form-item>
       <el-form-item prop="confirmPassword">
        <el-input 
          type="password"
          show-password
          placeholder="请再次确认新密码" 
          v-model="passwordData.confirmPassword"
        />
       </el-form-item>
       <el-form-item>
        <el-button @click="updatePassword">修改密码</el-button>
       </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import useLoginStore from '@/stores/login/login'
import { BASE_URL } from '@/utils/service' // 引入基本路径
import { updateUser, getUserById } from '@/api/user/user.js'
import { setCache, removeCache } from '@/utils/storage'
import { updatePass } from '@/api/user/user.js'

const loginStore = useLoginStore()
const router = useRouter()

const userInfo = loginStore.userInfo // 用户信息
const user = reactive({})

const isEdit = ref(false)

Object.keys(userInfo).forEach(key => {
  if (key !== 'password') {
    user[key] = userInfo[key]
  }
})

/**
 * 修改编辑状态
 */
const changeEditState = async () => {
  if (isEdit.value) {
    // 请求修改数据
    await updateUser(user)
    ElMessage.success('更新用户信息成功')
    let res = await getUserById(user.id)
    // 重新写入 pinia与 localStorage
    removeCache('userInfo')
    loginStore.setUserInfo(res)
    setCache('userInfo', res)
  }
  isEdit.value = !isEdit.value
}

/**
 * 上传头像成功
 */
const uploadAvatarFile = (response) => {
  if (response.code === 200) {
    user.avatarUrl = response.data
  } else {
    ElMessage.error(response.message)
  }
}

const passwordRef = ref() // 表单ref
const passwordData = reactive({
  password: '',
  newPassword: '',
  confirmPassword: ''
})

// 校验规则
const validatePass = (rule, value, callback) => {
  if (value != passwordData.newPassword) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

// 校验规则
const rules = reactive({
  password: [
    { required: true, message: '原密码不能为空', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '新密码不能为空', trigger: 'blur' },
  ],
  confirmPassword: [
    { validator: validatePass, trigger: 'blur' }
  ]
})

// 修改密码
const updatePassword = () => {
  // 表单校验
  passwordRef.value.validate(async isOK => {
    if (isOK) {
      // 校验通过 封装参数对象
      const data = {
        id: loginStore.userInfo.id, // 用户id
        password: passwordData.password, // 旧密码
        newPassword: passwordData.newPassword // 新密码
      }
      // 请求接口 修改密码
      const res = await updatePass(data)
      if (res !== null) {
        // 修改失败
        ElMessage.error(res)
      } else {
        // 修改成功后 清楚缓存
        removeCache('token')
        removeCache('userInfo')
        loginStore.setToken('')
        loginStore.setUserInfo('')
      }
      // 返回登录页
      router.push('/login')
    }
  })
}

</script>

<style scoped lang="less">
.container {
  display: flex;

  .user-info {
    flex: 6;
    padding: 20px;
  }

  .reset-password {
    flex: 5;
    padding: 20px;
    margin: 0 25px;

    .title {
      font-weight: 700;
      padding: 0 20px 20px 0;
    }
  }
}

/deep/.el-descriptions__cell {
  height: 12vh;

  .el-input {
    width: 100%;
    
    .el-input__wrapper {
      box-shadow: none;
      background-color: transparent;

      .el-input__inner {
        color: #000;
        -webkit-text-fill-color: #000;

        &[disabled] {
          cursor: default;
        }
      }
    }
  }
}
/deep/.el-form-item__content {
  margin-bottom: 15px;

  .el-input {
    width: 100%;
    cursor: default;

    .el-input__wrapper {
      box-shadow: none;
      border-bottom: 1px solid #ccc;
      border-radius: 0;

      .el-input__inner {
        letter-spacing: 5px;
      }

    }
  }
  .el-form-item__error {
    margin-left: 10px;
    letter-spacing: 5px;
  }

  .el-button {
    letter-spacing: 5px;
    width: 25%;
    height: 35px;
  }
}
</style>
