import { defineStore } from 'pinia'
import { getCache } from '@/utils/storage'

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
    loadLocalDataAction() {
      this.token = getCache('token')
      this.userInfo = getCache('userInfo')
    }
  }
})

export default useLoginStore