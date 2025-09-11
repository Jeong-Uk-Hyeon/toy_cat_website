/* Vue로 만든 프로그램의 진입점 전체 앱 초기화 
App.vue를 root component로 등록*/
 
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

createApp(App).use(store).use(router).mount('#app')
