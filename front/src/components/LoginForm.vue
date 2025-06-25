<template>
  <div class="login-form">
    <h2>로그인</h2>
    <form @submit.prevent="onSubmit">
      <div class="form-group">
        <label for="username">아이디</label>
        <input
          id="username"
          v-model="username"
          type="text"
          placeholder="아이디를 입력하세요"
          required
          autocomplete="username"
        />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input
          id="password"
          v-model="password"
          type="password"
          placeholder="비밀번호를 입력하세요"
          required
          autocomplete="current-password"
        />
      </div>

        <div class="remember-me">
        <label for="remember">
            <input type="checkbox" id="remember" />
            <span>아이디 저장</span>
        </label>
        </div>

      <button type="submit" class="login-btn">로그인</button>
    </form>

    <div class="helper-links">
    <a href="#">아이디/비밀번호 찾기</a>
    <span>|</span>
    <router-link to="/register/agree">회원가입</router-link>
    </div>

    <div class="divider">
    <hr />
        <span> OR </span>
        <hr />
    </div>

    <div class="social-login">
        <img src="@/assets/loginBtn/kakao_login.png" alt="카카오 로그인" class="social-icon" />
        <img src="@/assets/loginBtn/naver_login.png" alt="네이버 로그인" class="social-icon" />
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const username = ref('')
const password = ref('')

function onSubmit() { //미연결
  alert(`아이디: ${username.value}\n비밀번호: ${password.value}`)
}

function socialLogin(provider) { //미연결
  alert(`${provider} 로그인 버튼 클릭됨`)
}
</script>

<style scoped>
.login-form {
  max-width: 320px;
  margin: 40px auto;
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-family: 'Noto Sans KR', sans-serif;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 6px;
  font-weight: 600;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid #bbb;
  border-radius: 4px;
  box-sizing: border-box;
}

.login-btn {
  width: 100%;
  padding: 12px 0;
  background-color: #c8ad7f;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: 700;
  font-size: 16px;
  cursor: pointer;
}

.login-btn:hover {
  background-color: #c0a06a;
}

.social-login {
  display: flex;
  justify-content: center;
  margin: 20px;
  align-items: center;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
}

.divider hr {
  flex: 1;
  border: none;
  border-top: 1px solid #ccc;
}

.divider span {
  text-align: center;
  color: #666;
  padding: 10px;
}

.social-icon {
box-sizing: border-box;
width: 150px;
cursor: pointer;
display: block; 
padding: 0 5px;
}

.helper-links {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin: 12px 0;
  font-size: 13px;
}

.helper-links a {
  color: #007bff;
  text-decoration: none;
}

.helper-links a:hover {
  text-decoration: underline;
}

.remember-me {
  margin-bottom: 16px;
}

.remember-me label {
  display: inline-flex;
  align-items: center;
  font-size: 13px;
  color: #333;
  cursor: pointer;
}

.remember-me input[type="checkbox"] {
  width: 16px;
  height: 16px;
  margin-right: 6px;
  vertical-align: middle;
}

.remember-me span {
  line-height: 1;
}
</style>
