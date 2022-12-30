import { createRouter, createWebHashHistory } from 'vue-router'
import useLoginStore from '@/stores/login/login'

// 在这里时候pinia还没有被注册
// const loginStore = useLoginStore()

const routes = [
  {
    path: '/',
    redirect: '/home', // 重定向至首页
    name: 'Main',
    meta: {
      title: '核心布局'
    },
    component: () => import('@/layout'),
    children: [
      {
        path: 'home',
        name: 'Home',
        meta: {
          title: '首页',
          icon: 'House',
          isHidden: false,
          root: '/'
        },
        component: () => import('@/views/home')
      },
      {
        path: 'user',
        name: 'User',
        meta: {
          title: '角色管理',
          icon: 'User',
          isHidden: false,
          root: 'sys'
        },
        component: () => import('@/views/user')
      },
      {
        path: 'file',
        name: 'File',
        meta: {
          title: '文件管理',
          icon: 'Document',
          isHidden: false,
          root: 'sys'
        },
        component: () => import('@/views/FileManage')
      },

      // 不在侧边栏
      {
        path: 'person',
        name: 'PersonalCenter',
        meta: {
          title: '个人中心',
          isHidden: true,
        },
        component: () => import('@/views/PersonalCenter')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录'
    },
    component: () => import('@/views/login')
  },
  {
    path: '/404',
    name: '404',
    meta: {
      title: '找不到页面'
    },
    component: () => import('@/views/NotFound')
  },
  {
    path: '/:pathMatch(.*)',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 定义白名单, 不需要token的页面
const whiteList = ['/login', '/404']

// 前置路由守卫
router.beforeEach(to => {
  const loginStore = useLoginStore()

  // 判断有没有token
  if (loginStore.token) {
    // 前往登录页
    if (to.path === '/login') {
      // 跳转至首页
      return '/'
    } else {
      return 
    }
  } else {
    if (whiteList.includes(to.path)) {
      // 前往白名单
      return 
    } else {
      return '/login'
    }
  }
})

// 后置路由守卫
router.afterEach(to => {
  // 修改title
  document.title = to.meta.title
})

export default router
