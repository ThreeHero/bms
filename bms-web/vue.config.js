const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    //vue自己启动的端口
    port: 9001
  },

  chainWebpack: config => {
    config.plugin('html')
      .tap(args => {
        args[0].title = '后台管理系统'
        return args
    })
  }
})
