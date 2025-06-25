<template>
  <header class="header">
    <!-- 로고 / 배너 영역 -->
    <div class="logo-area">
      <img src="@/assets/logo.png" alt="TripTalk 로고" class="logo-img" />
    
    </div>

    <!-- 버튼 / 네비게이션 영역 -->
    <nav class="nav-buttons">
      <router-link to="/boardList" class="nav-btn">여행 게시판</router-link>
      <router-link to="/register/agree" class="nav-btn">회원가입</router-link>
      <router-link to="/loginForm" class="nav-btn">로그인</router-link>
      <router-link to="/myPage" class="nav-btn">마이페이지</router-link>
    </nav>
  </header>
</template>

<script setup>
// 동적 데이터 없음
</script>

<style scoped>
.header {
  width: 100%;
  height: 250px;
  background-color: #f6f2ec; /* 밝은 베이지 */
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 65px;
  box-sizing: border-box;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 왼쪽 로고 */
.logo-area {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 400px;

  object-fit: contain;
  margin-right: 12px;
}

/* 오른쪽 네비 버튼 */
.nav-buttons {
  display: flex;
  gap: 20px;
}

.nav-btn {
  color: #2c3e50; /* ✅ 진한 남색으로 글씨 */
  text-decoration: none;
  font-size: 16px;
  padding: 8px 14px;
  border: 1px solid #2c3e50; /* ✅ 테두리도 동일한 색상 */
  border-radius: 6px;
  transition: background 0.3s, color 0.3s;
  background-color: transparent;
}

.nav-btn:hover {
  background-color: #2c3e50;
  color: #ffffff;
  font-weight: bold;
}
</style>