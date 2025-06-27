<template>
  <header class="header">
    <!-- 로고 / 배너 영역 -->
    <div class="logo-area">
      <img src="@/assets/logo.png" alt="TripTalk 로고" class="logo-img" />
    </div>

    <!-- 햄버거 버튼 (마우스 호버용) -->
    <div class="burger-wrapper">
      <button class="burger">
        ☰
      </button>

      <!-- 버튼 / 네비게이션 영역 -->
      <nav class="nav-buttons">
        <router-link to="/product" class="nav-btn">상품 게시판</router-link>
        <router-link to="/boardList" class="nav-btn">여행 게시판</router-link>
        <router-link to="/register/agree" class="nav-btn">회원가입</router-link>
        <router-link to="/loginForm" class="nav-btn">로그인</router-link>
        <router-link to="/MyPage" class="nav-btn">마이페이지</router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
// ref, toggle 함수 삭제 (불필요해짐)
</script>

<style scoped>
.header {
  width: 100%;

  background-color: #f6f2ec;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  padding: 20px 5vw;
  box-sizing: border-box;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.logo-area {
  display: flex;
  align-items: center;
  flex: 1 1 300px;
}

.logo-img {
  width: 100%;
  max-width: 250px;
  object-fit: contain;
}

/* 햄버거와 메뉴 묶음 */
.burger-wrapper {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

/* 햄버거 버튼 */
.burger {
  font-size: 28px;
  background: none;
  border: none;
  cursor: pointer;
  color: #2c3e50;
  display: none;
}

/* 버튼 영역 */
.nav-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: flex-end;
  align-items: center;
  flex: 1 1 300px;
}

.nav-btn {
  color: #2c3e50;
  text-decoration: none;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  background-color: transparent;
  transition: background 0.3s, color 0.3s;
}

.nav-btn:hover {
  background-color: #2c3e50;
  color: #ffffff;
}

/* 반응형 - 모바일 대응 */
@media (max-width: 768px) {
  .burger {
    display: block;
  }

  .nav-buttons {
    display: none;
    flex-direction: column;
    background-color: #f6f2ec;
    position: absolute;
    top: 50px;
    right: 0;
    width: 160px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    border-radius: 6px;
    z-index: 10;
  }

  .burger-wrapper:hover .nav-buttons {
    display: flex;
  }

  .nav-btn {
    width: 100%;
    text-align: left;
    padding: 12px 20px;
    border-radius: 0;
  }

  .header {
    flex-direction: row;
    align-items: center;
  }
}
</style>