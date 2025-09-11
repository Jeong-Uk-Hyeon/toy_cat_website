/*Vue router 설정 파일 
router를 이용해서 페이지 간의 이동이 가능하게 함
경로에 따라 어떤 컴포넌트를 표시할지 정의*/

import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue' // 라우터에 컴포넌트 연결하는 코드 
import MainView from '../views/MainView.vue'

const routes = [
  {
    path: '/',  // 가상 경로(url)
    name: 'Login',
    component: LoginView // component이름
  },
  
  {
    path: '/main', 
    name: 'Main', 
    component: MainView }
  
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
