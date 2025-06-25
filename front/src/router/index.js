import { createRouter, createWebHistory } from 'vue-router'
import HelloWorld from '@/components/HelloWorld.vue'
import LoginForm from '@/components/LoginForm.vue'
import RegisterAgree from '@/components/register/RegisterAgree.vue'
import RegisterInfo from '@/components/register/RegisterInfo.vue'
import RegisterComplete from '@/components/register/RegisterComplete.vue'
import Mypage from '@/components/mypage/Mypage.vue'

const routes = [
  { path: '/', name: 'HelloWorld', component: HelloWorld } , //메인
  { path: '/loginForm', name: 'LoginForm', component: LoginForm } , //로그인
  { path: '/register/agree', name: 'RegisterAgree', component: RegisterAgree }, //회원가입폼(약관동의)
  { path: '/register/info', name: 'RegisterInfo', component: RegisterInfo }, //회원가입폼(가입정보입력)
  { path: '/register/complete', name: 'RegisterComplete', component: RegisterComplete }, //회원가입폼(완료페이지)
  { path: '/mypage', name: 'Mypage', component: Mypage }, //마이페이지



]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router