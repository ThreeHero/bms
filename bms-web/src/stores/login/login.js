import { defineStore } from 'pinia'
import { getCache } from '@/utils/storage'
import { getRouter } from '@/router'


const useLoginStore = defineStore('login', {
  state() {
    return {
      token: '',
      userInfo: ''
    }
  },
  getters: {
    avatarUrl(state) {
      return state.userInfo?.avatarUrl
    }
  },
  actions: {
    setUserInfo(value) {
      this.userInfo = value
    },
    setToken(token) {
      this.token = token
    },

    // 加载本地数据
    async loadLocalDataAction() {
      this.token = getCache('token')
      this.userInfo = getCache('userInfo')

      getRouter(getCache('router')?? [])

    }
  }
})

export default useLoginStore