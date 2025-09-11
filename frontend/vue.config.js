const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  outputDir: '../src/main/resources/static', // 빌드 타겟 디렉토리 프론트엔드를 빌드
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 8000 (스프링 서버) 로 보낸다
        target: 'http://localhost:8000',
        changeOrigin: true // cross origin 허용
      }
    }
  }
}