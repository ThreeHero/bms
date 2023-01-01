<template>

  <!-- 搜索区域 -->
  <div class="search">
    <el-input placeholder="用户名" v-model="user.username">
      <template #suffix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="昵称" v-model="user.nickname">
      <template #suffix>
        <el-icon><User /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="地址" v-model="user.address">
      <template #suffix>
        <el-icon><Location /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="邮箱" v-model="user.email">
      <template #suffix>
        <el-icon><Message /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="手机" v-model="user.phone">
      <template #suffix>
        <el-icon><Iphone /></el-icon>
        </template>
    </el-input>
    <el-button plain @click="userSearch">搜 索</el-button>
    <el-button plain type="danger" @click="reset">重 置</el-button>
  </div>

  <!-- 按钮功能区 -->
  <div class="btns">
    <el-button type="primary" @click="saveUserInfo">
      <el-icon style="margin-right: 5px"><CirclePlusFilled /></el-icon>
      新 增
    </el-button>
    <el-upload 
      :action="BASE_URL + '/user/import'" 
      :show-file-list="false" 
      accept=".xlsx"
      :on-success="userImport"
      style="padding: 0 5px"
    >
      <el-button type="primary">
        <el-icon style="margin-right: 5px"><Download /></el-icon>
        导 入
      </el-button>
    </el-upload>
    <el-button type="primary" @click="userExport">
      <el-icon style="margin-right: 5px"><Upload /></el-icon>
      导 出
    </el-button>
  </div>

  <!-- 表格区域 -->
  <el-table :data="userList" border>
    <el-table-column type="index" align="center" />
    <el-table-column prop="username" label="用户名" />
    <el-table-column prop="nickname" label="昵称" />
    <el-table-column prop="role" label="角色" />
    <el-table-column prop="email" label="邮箱" />
    <el-table-column prop="phone" label="手机" />
    <el-table-column prop="address" label="地址" />
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <!-- 编辑按钮 -->
        <el-button type="primary" @click.prevent="editRow(scope.row)">
          <el-icon>
            <Edit />
          </el-icon>
          <span>编辑</span>
        </el-button>
        <!-- 删除按钮 -->
        <el-button type="danger" @click.prevent="deleteRow(scope.row)">
          <el-icon>
            <Delete />
          </el-icon>
          <span>删除</span>
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页区域 -->
  <div class="pagination">
    <el-pagination
      layout="sizes, prev, pager, next, total"
      :page-sizes="[5, 10, 15, 20]"
      :total="total"
      :default-page-size="pageSize"
      :current-page="page"
      @current-change="changeCurrentPage"
      @size-change="changePageSize"
    />
  </div>

  <!-- 弹窗 -->
  <el-dialog v-model="isShowDialog" :title="dialogTitle" width="350px" @close="cancelEdit">
    <el-form :model="userInfo" label-width="80px" :rules="rules" ref="formRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="userInfo.username" :disabled="dialogTitle === '修改'" />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="userInfo.nickname" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userInfo.email" />
      </el-form-item>
      <el-form-item label="手机" prop="phone">
        <el-input v-model="userInfo.phone" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="userInfo.address" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span>
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="updateOrSaveUser">
          保存
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { BASE_URL } from '@/utils/service' // 引入基本路径
import { ref, reactive } from 'vue'
import { setCache,  getCache } from '@/utils/storage'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  saveUser,
  removeUser,
  updateUser, 
  getUserById,
  getUserList,
} from '@/api/user/user.js'


const formRef = ref(null)
const dialogTitle = ref('新增') // 弹层标题
const isShowDialog = ref(false) //  控制弹窗开关
const userList = reactive([]) // 用户列表
const page = ref(1) // 当前页数
const pageSize = ref(getCache('pageSize', false) ?? 5) // 每页容量
const total = ref(0) // 用户总数
// 用户信息
const user = reactive({
  username: '',
  nickname: '',
  role: '',
  address: '',
  email: '',
  phone: ''
})
// 单个用户信息
let userInfo = reactive({
  username: '',
  nickname: '',
  role: '',
  address: '',
  email: '',
  phone: ''
})
// 校验规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    {
      pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
      message: '格式错误',
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    {
      pattern: /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/,
      message: '格式错误',
      trigger: 'blur'
    }
  ],
})

// 封装分页查询
const getPage = async (userInfo) => {
  let params = {}
  if (userInfo) {
    params = {
      page: page.value,
      pageSize: pageSize.value,
      ...userInfo
    }
  } else {
    params = {
      page: page.value,
      pageSize: pageSize.value,
    }
  }
  const res = await getUserList(params)

  if (typeof res !== 'string') {
    // 先清空 用户列表
    while (userList.length) {
      userList.pop()
    }
    const roleArr = ['管理员', '会员用户', '普通用户']
    for (const user of res.records) {
      user.role = roleArr[user.roleId - 1]
      userList.push(user)
    }
    total.value = res.total
  } else {
    ElMessage.error('token过期了 请重新登录')
  }
}

// 修改当前页
const changeCurrentPage = (currentPage) => {
  page.value = currentPage
  getPage()
}
// 修改大小
const changePageSize = (pageS) => {
  pageSize.value = pageS
  setCache('pageSize', pageSize.value, false)
  getPage()
}
// 分页查询
getPage()

// 删除
const deleteRow = ({ id }) => {
  ElMessageBox.confirm(
    '删除用户后无法恢复，请确认!',
    '确认删除',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(async () => {
    // 删除当前行并重新请求数据
    await removeUser(id)
    getPage()
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
  })
  .catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}
// 编辑
const editRow = async ({ id }) => {
  // 查询当前id信息
  const res = await getUserById(id)
  Object.keys(userInfo).forEach(key => {
    userInfo[key] = res[key]
  })
  userInfo.id = id
  isShowDialog.value = true
  dialogTitle.value = '修改'
}
// 新增
const saveUserInfo = () => {

  Object.keys(userInfo).forEach(key => {
    userInfo[key] = ''
  })
  userInfo.id = ''
  isShowDialog.value = true
  dialogTitle.value = '新增'
}
// 模糊查询
const userSearch = () => {
  getPage(user)
}
// 重置
const reset = () => {
 Object.keys(user).forEach(key => {
    user[key] = ''
  })
  getPage()
}
// 新增 或 编辑
const updateOrSaveUser = () => {
  formRef.value.validate(async isOK => {
    if (isOK) {
      if (dialogTitle.value === '新增') {
        await saveUser(userInfo)
      } else {
        await updateUser(userInfo)
      }
      ElMessage({
        type: 'success',
        message: '保存成功',
      })
      getPage()
      isShowDialog.value = false
    } else {
      ElMessage({
        type: 'error',
        message: '保存失败',
      })
    }
  })
}
// 取消编辑
const cancelEdit = () => {
  isShowDialog.value = false
  formRef.value.resetFields()
}

// 导出
const userExport = () => {
  window.open(BASE_URL + '/user/export')
  ElMessage({
    type: 'success',
    message: '导出成功',
  })
}
// 导入
const userImport = () => {
  ElMessage({
    type: 'success',
    message: '导入成功',
  })
  getPage()
}

</script>

<style scoped lang="less">
// 按钮
.search, .btns {
  margin-bottom: 20px;
}
.btns {
  display: flex;
}


</style>
