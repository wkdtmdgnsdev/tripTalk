<template>
    <div class="travellog">
        <h3>여행 로그 </h3>
        
        <router-link to="/board/itineraryBot" class="nav-btn">일정 챗봇</router-link>

        <!-- 추천 수 대로 보이기-->
        <div class="section">
            <div class="section-header">
                <h3>최다 추천 글 </h3>
                <div class="nav-buttons">
                    <button @click="prev('posts')" :disabled="postPage === 0"></button>
                    <button @click="next('posts')" :disabled="postPage >= maxPage('posts')"></button>
                </div>
            </div>
            <div class="card-list">
                <div v-for="(item, index) in paginatedPosts" :key="item.id || 'more-posts' + index" class="card"
                    :class="{ 'more-card': item.isMore }"
                    @click="item.isMore ? goToDetail('posts') : null">
                    <template v-if="!item.isMore">
                        <div class="card-title-wrapper">
                            <p class="card-title" :title="item.title">{{ item.title }}</p>
                        </div>
                    </template>
                    <template v-else>
                        <div class="more-text">…</div>
                    </template>
                </div>
            </div>
        </div>

        <!-- 글 목록 출력 -->
        <div class="section">
            <div>
                <ul class="post-list">
                    <li v-for="(log, idx) in allPosts" :key="log.id" @click="goToLogDetail(log.id)" class="log-item">
                        <span class="log-title">{{log.title}}</span>
                        <span class="log-date">{{formatDate(log.createdAt)}}</span>
                    </li>
                </ul>
            </div>
        </div>    
  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue'
import {useRoute} from 'vue-router'
import axios from 'axios'

const router = useRoute()

// 페이징 관련 데이터
const postPage = ref(0)
//추천글 5개
const postsPerPage = 5
//실제 log (DB에서 가져온 데이터로 대체됨)
const allPosts = ref([])

//DB로 부터 글 가져오기
const fetchPosts = async() =>{
    try{
        const response = await axios.get('http://localhost:8080/api/travellog')
        allPosts.value = response.data
    } catch (error){
        console.error("글 목록 가져오기 실패")
    }
}

onMounted(()=>{
    fetchPosts()
})

// 추천글 페이징
const paginatedPosts = computed(() => {
const start = postPage.value * postsPerPage
const end = start + postsPerPage
const page = allPosts.value.slice(start, end)
  return page.length === postsPerPage
    ? [...page, { isMore: true }]
    : page
})

const maxPage = () => Math.floor(allPosts.value.length / postsPerPage)
const next = (type) => {
  if (type === 'posts' && postPage.value < maxPage()) postPage.value++
}
const prev = (type) => {
  if (type === 'posts' && postPage.value > 0) postPage.value--
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString()
}

//특정 글 보기(클릭 시 해당 글로 들어감)
const goToLogDetail = (id) => {
    router.push('/board/${id}')
}


</script>

<style scoped>
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