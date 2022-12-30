import { createApp } from 'vue'
// 全局加载element组件
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'
import store from '@/stores'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

import 'element-plus/dist/index.css'
import '@/assets/style/index.less'


const app = createApp(App)
app.use(store)
app.use(router)

app.use(ElementPlus, {
  locale: zhCn,
})

// 注册icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
