<template>
    <div class="register-info">
    <div class="stepper">
      <div :class="['step', { active: currentStep === 1 }]">1. 약관 동의</div>
      <div :class="['step', { active: currentStep === 2 }]">2. 회원 정보 입력</div>
      <div :class="['step', { active: currentStep === 3 }]">3. 완료</div>
    </div>
  
  <div class="register-complete">
    <h2>회원가입 완료</h2>
    <p class="message">
      <strong>{{ userEmail }}</strong>로 인증 메일을 보냈습니다.<br />
      이메일을 확인하시고 인증을 완료하셔야<br />
      로그인하실 수 있습니다.
    </p>

    <div class="btn-group">
      <button class="main-btn" @click="goMain">메인으로</button>
      <button class="login-btn" @click="goLogin">로그인</button>
    </div>

    <div class="email-help">
      <span>인증 메일을 받지 못했거나 문제가 발생했나요? </span>
      <button class="reissue-btn" @click="goReissue">재발급 요청</button>
    </div>
  </div>
  
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const currentStep = 3

const userEmail = route.query.email || 'aaaaaa@aaaa.com'

function goMain() {
  router.push('/')  // 메인 페이지 경로에 맞게 수정하세요
}

function goLogin() {
  router.push('/login')  // 로그인 페이지 경로에 맞게 수정하세요
}

function goReissue() {
  router.push('/email/reissue')  // 이메일 재발급 요청 페이지 경로로 수정하세요
}
</script>

<style scoped>
.register-info {
  max-width: 480px;
  margin: 40px auto;
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: 'Noto Sans KR', sans-serif;
}

.stepper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.step {
  flex: 1;
  text-align: center;
  font-weight: 600;
  color: #999;
  padding-bottom: 8px;
  border-bottom: 2px solid #ddd;
  user-select: none;
}

.step.active {
  color: #c8ad7f;
  border-color: #c8ad7f;
}

.register-complete {
  max-width: 480px;
  margin: 60px auto;
  padding: 36px 32px;
  font-family: 'Noto Sans KR', sans-serif;
  text-align: center;
}

h2 {
  color: #c8ad7f;
  font-weight: 800;
  font-size: 28px;
  margin-bottom: 28px;
  letter-spacing: 1px;
}

.message {
  font-size: 17px;
  color: #4a4a4a;
  line-height: 1.6;
  margin-bottom: 32px;
  user-select: text;
  border-top: 1px solid #c8ad7f33;
  padding-top: 40px;
  padding-bottom: 20px;
}

.message strong {
  color: #a37a44;
  word-break: break-word;
}

.btn-group {
  display: flex;
  justify-content: center;
  gap: 16px; /* 간격 살짝 줄임 */
  margin-bottom: 24px; /* 아래 여백 줄임 */
}

.main-btn,
.login-btn {
  flex: 1;
  min-width: 120px; /* 최소 너비 지정 */
  padding: 14px 0;
  font-weight: 700;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 3px 6px rgba(200, 173, 127, 0.4);
  user-select: none;
}

.main-btn {
  background-color: #f5f0e6;
  border: 2px solid #c8ad7f;
  color: #7b6845;
}

.main-btn:hover {
  background-color: #c8ad7f;
  color: white;
  box-shadow: 0 5px 12px rgba(200, 173, 127, 0.7);
}

.login-btn {
  background-color: #c8ad7f;
  border: none;
  color: white;
}

.login-btn:hover {
  background-color: #b79965;
  box-shadow: 0 5px 12px rgba(183, 153, 101, 0.7);
}

.email-help {
  font-size: 14.5px;
  color: #666;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  user-select: none;
  margin-bottom: 0;
}

.reissue-btn {
  background: none;
  border: none;
  color: #c8ad7f;
  font-weight: 700;
  cursor: pointer;
  text-decoration: underline;
  padding: 0;
  font-size: 14.5px;
  transition: color 0.3s ease;
}

.reissue-btn:hover {
  color: #b79965;
}
</style>
