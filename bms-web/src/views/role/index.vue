<template>
  <div>
    <!-- 功能 -->
    <role-search
      @role-button="roleButton"
      @save-role="saveRoleInfo"
    />

    <!-- 表格 -->
    <el-table :data="roleList" border>
      <el-table-column type="index" align="center" />
      <el-table-column label="名称" prop="name" />
      <el-table-column label="描述" prop="description" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="success" @click="assignPermissions(scope.row)">
            <el-icon><Menu /></el-icon>
            <span>分配权限</span>
          </el-button>
          <el-button type="primary" @click="updateRoleInfo(scope.row)">
            <el-icon><Edit /></el-icon>
            <span>编辑</span>
          </el-button>
          <el-button type="danger" @click="deleteRole(scope.row)">
            <el-icon><Delete /></el-icon>
            <span>删除</span>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <role-pagination
      :page="page"
      :pageSize="pageSize"
      :total="total"
      @change-current-page="changeCurrentPage"
      @change-page-size="changePageSize"
    />

    <!-- 增 改 弹窗 -->
    <el-dialog
      v-model="isShowDialog"
      :title="saveOrUpdateTitle"
      width="350px"
      draggable
      destroy-on-close
    >
      <el-form label-width="80px" :model="roleInfo" :rules="rules" ref="roleFormRef">
        <el-form-item label="名称" prop="name">
          <el-input v-model="roleInfo.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" style="width: 200px" v-model="roleInfo.description" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span>
          <el-button @click="isShowDialog = false">取消</el-button>
          <el-button type="primary" @click="saveOrUpdateRole">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 分配权限 -->
    <el-dialog
      v-model="isShow"
      title="分配权限"
      width="30%"
      destroy-on-close
    >
      <el-tree 
        :data="treeData" 
        :props="{
          label: 'title',
          children: 'children'
        }" 
        default-expand-all
      />
      <template #footer>
        <span>
          <el-button @click="isShow = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAll, getMenuByRoleId } from '@/api/menu/menu.js'
import { getRoleList, getRoleById, saveRole, updateRole, removeRole } from '@/api/role/role.js'
import { Menu } from '@element-plus/icons-vue'
import RolePagination from './components/RolePagination'
import RoleSearch from './components/RoleSearch'
import useToTree from '@/hooks/useToTree'

const roleList = reactive([])
const page = ref(1) // 当前页数
const pageSize = ref(5) // 每页容量
const total = ref(0) // 总条数
const isShowDialog = ref(false)// 增 改 弹窗
const saveOrUpdateTitle = ref('') 
const isShow = ref(false)
const roleFormRef = ref()
const roleInfo = reactive({
  name: '',
  description: ''
})
const rules = reactive({
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '描述不能为空', trigger: 'blur' }
  ]
})
const treeData = reactive([])
const defaultCheckKey = reactive([])

// 请求菜单列表
const getMenuList = async () => {
  const res = await getAll()
  const data = useToTree(res)
  data.forEach(item => treeData.push(item))
}
getMenuList()

// 封装数据请求
const getPage = async (name) => {
  let params = {}
  if (name) {
    params = {
      page: page.value,
      pageSize: pageSize.value,
      name
    }
  } else {
    params = {
      page: page.value,
      pageSize: pageSize.value,
    }
  }

  const res = await getRoleList(params)

  total.value = res.total

  while (roleList.length) {
    roleList.pop()
  }
  for (const role of res.records) {
    roleList.push(role)
  }

}
getPage()

const changeCurrentPage = (currentPage) => {
  page.value = currentPage
  getPage()
}
const changePageSize = (pageS) => {
  pageSize.value = pageS
  getPage()
}

const roleButton = (name) => {
  getPage(name)
}
// 新增角色
const saveRoleInfo = () => {
  Object.keys(roleInfo).forEach(key => {
    if (key !== 'id') {
      roleInfo[key] = ''
    } else {
      roleInfo[key] = undefined
    }
  })
  saveOrUpdateTitle.value = '新增'
  isShowDialog.value = true
}
// 修改角色
const updateRoleInfo = async ({ id }) => {
  const res = await getRoleById(id)
  Object.keys(res).forEach(key => roleInfo[key] = res[key])
  saveOrUpdateTitle.value = '修改'
  isShowDialog.value = true
}
const saveOrUpdateRole = () => {
  // 校验
  roleFormRef.value.validate(async isOK => {
    if (isOK) {
      // 判断新增还是修改
      if (saveOrUpdateTitle.value === '新增') {
        await saveRole(roleInfo)
      } else {
        await updateRole(roleInfo)
      }
      ElMessage.success(`${saveOrUpdateTitle.value}成功`)
      getPage()
      isShowDialog.value = false
    } else {
      ElMessage.error(`${saveOrUpdateTitle.value}失败`)
    }
  })
}
// 删除角色
const deleteRole = ({ id }) => {
  ElMessageBox.confirm(
    '删除角色后无法恢复，请确认!',
    '确认删除',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then(async () => {
    // 删除当前行并重新请求数据
    await removeRole(id)
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

// 分配权限
const assignPermissions = async ({ id }) => {
 
  isShow.value = true
}



</script>

<style scoped lang="less">

</style>
