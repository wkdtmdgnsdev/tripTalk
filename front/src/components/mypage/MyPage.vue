<template>
  <div class="mypage-container">
    <!-- 프로필 -->
    <div class="profile-box">
      <img :src="profileImage" class="profile-img" />
      <div class="profile-info">
        <h2 class="nickname" :title="nickname">{{ nickname }}</h2>
        <button @click="changeProfile" class="change-btn">사진 변경</button>
        <input type="file" ref="fileInput" @change="onImageSelected" accept="image/*" class="hidden" />
      </div>
    </div>

    <!-- 예약 내역 섹션 -->
    <div class="section">
      <div class="section-header">
        <h3>예약 내역</h3>
        <div class="nav-buttons">
          <button @click="prev('reservations')" :disabled="reservationPage === 0">‹</button>
          <button @click="next('reservations')" :disabled="reservationPage >= maxPage('reservations')">›</button>
        </div>
      </div>
      <div class="card-list">
        <div
          v-for="(item, index) in paginatedReservations"
          :key="item.id || 'more-reservations'"
          class="card"
          :class="{ 'more-card': item.isMore }"
          @click="item.isMore ? goToDetail('reservations') : null"
        >
          <template v-if="!item.isMore">
            <div class="card-title-wrapper">
              <p class="card-title" :title="item.title">{{ item.title }}</p>
            </div>
            <p class="card-date">{{ item.date }}</p>
          </template>
          <template v-else>
            <div class="more-text">… 더보기</div>
          </template>
        </div>
      </div>
    </div>

    <!-- 내가 쓴 글 섹션 -->
    <div class="section">
      <div class="section-header">
        <h3>내가 쓴 글</h3>
        <div class="nav-buttons">
          <button @click="prev('posts')" :disabled="postPage === 0">‹</button>
          <button @click="next('posts')" :disabled="postPage >= maxPage('posts')">›</button>
        </div>
      </div>
      <div class="card-list">
        <div
          v-for="(item, index) in paginatedPosts"
          :key="item.id || 'more-posts'"
          class="card"
          :class="{ 'more-card': item.isMore }"
          @click="item.isMore ? goToDetail('posts') : null"
        >
          <template v-if="!item.isMore">
            <div class="card-title-wrapper">
              <p class="card-title" :title="item.title">{{ item.title }}</p>
            </div>
          </template>
          <template v-else>
            <div class="more-text">… 더보기</div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const profileImage = ref('/img/default-profile.jpg')
const nickname = ref('나의 닉네임')
const fileInput = ref(null)

function changeProfile() {
  fileInput.value.click()
}
function onImageSelected(e) {
  const file = e.target.files[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = (ev) => {
    profileImage.value = ev.target.result
  }
  reader.readAsDataURL(file)
}

const reservations = ref([
  { id: 1, title: '부산 여행', date: '2025-07-01' },
  { id: 2, title: '서울 호텔', date: '2025-08-12' },
  { id: 3, title: '서울 호텔2', date: '2025-08-12' },
  { id: 4, title: '서울 호텔3', date: '2025-08-12' },
  { id: 5, title: '서울 호텔4', date: '2025-08-12' },
  { id: 6, title: '서울 호텔5', date: '2025-08-12' },
])

const posts = ref([
  { id: 1, title: '부산 맛집 후기' },
  { id: 2, title: '서울 여행기1' },
  { id: 3, title: '서울 여행기2' },
  { id: 4, title: '서울 여행기3' },
  { id: 5, title: '서울 여행기4' },
  { id: 6, title: '서울 여행기5' },
  { id: 7, title: '서울 여행기6' },
])

const visibleCount = 3
const reservationPage = ref(0)
const postPage = ref(0)

function injectMoreCard(items, page) {
  const totalPages = Math.ceil(items.length / visibleCount)
  const start = page * visibleCount
  const end = start + visibleCount
  let currentItems = items.slice(start, end)

  const isLastPage = page === totalPages - 1
  const hasMore = items.length > visibleCount * totalPages - 1

  if (isLastPage && hasMore) {
    if (currentItems.length === visibleCount) {
      currentItems = [...currentItems.slice(0, visibleCount - 1), { isMore: true }]
    } else {
      currentItems = [...currentItems, { isMore: true }]
    }
  }
  return currentItems
}

const paginatedReservations = computed(() => injectMoreCard(reservations.value, reservationPage.value))
const paginatedPosts = computed(() => injectMoreCard(posts.value, postPage.value))

function maxPage(type) {
  const items = type === 'reservations' ? reservations.value : posts.value
  return Math.ceil(items.length / visibleCount) - 1
}

function next(type) {
  if (type === 'reservations' && reservationPage.value < maxPage(type)) reservationPage.value++
  if (type === 'posts' && postPage.value < maxPage(type)) postPage.value++
}
function prev(type) {
  if (type === 'reservations' && reservationPage.value > 0) reservationPage.value--
  if (type === 'posts' && postPage.value > 0) postPage.value--
}

function goToDetail(type) {
  if (type === 'reservations') {
    router.push('/reservations')
  } else if (type === 'posts') {
    router.push('/posts')
  }
}
</script>

<style scoped>
.mypage-container {
  max-width: 480px;
  margin: 40px auto;
  padding: 24px;
  font-family: 'Noto Sans KR', sans-serif;
  box-sizing: border-box;
}

.profile-box {
  display: flex;
  gap: 16px;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  background: #faf7f2;
  margin-bottom: 30px;
}
.profile-img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #ccc;
}
.profile-info {
  flex: 1;
  max-width: 360px;
}
.nickname {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.change-btn {
  font-size: 14px;
  color: #c8ad7f;
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: underline;
}
.hidden {
  display: none;
}

.section {
  margin-bottom: 32px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.section-header h3 {
  font-size: 18px;
  font-weight: 600;
}

.nav-buttons button {
  background: none;
  border: none;
  font-size: 22px;
  margin: 0 4px;
  color: #c8ad7f;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}
.nav-buttons button:hover:not(:disabled) {
  background-color: #f5efe3;
}
.nav-buttons button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.card-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.card {
  flex: 0 0 calc((100% - 24px) / 3);
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px;
  box-sizing: border-box;
  text-align: center;
  user-select: none;
}

.more-card {
  background: transparent !important;
  border: none !important;
  padding: 0 !important;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: 700;
  color: #c8ad7f;
  cursor: pointer;
  box-shadow: none !important;
}

.card-title-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.card-title {
  font-weight: 600;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin: 0;
  flex-grow: 1;
  text-align: left;
}

.card-date {
  font-size: 14px;
  color: #555;
  margin-top: 6px;
}
</style>
