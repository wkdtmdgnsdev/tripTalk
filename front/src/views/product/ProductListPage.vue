<template>
  <div class="product-list">
    <!-- 검색과 필터 고정 -->
    <div
    ref="wrapperRef"
    class="search-filter-wrapper"
    style="position: sticky; z-index: 999;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
    background-color: #f8f8f8">

    <div class="top-bar">
      <div class="input-box">
        <span class="icon">🔍</span>
        <input
          v-model="searchValue"
          type="text"
          class="search-input"
          placeholder="검색어"
          @keyup.enter="handleSearch"
        />
        <button v-if="searchValue" class="clear-btn" @click="searchValue = ''">✕</button>
      </div>

      <!-- 일정 선택 (팝업형 VueDatePicker 사용) -->
      <div class="input-box">
        <span class="icon">📅</span>
        <VueDatePicker
          v-model="dateRange"
          range
          :locale="ko"
          :enable-time-picker="false"
          :auto-apply="false"
          :min-date="new Date()"
          input-class-name="custom-date-input"
          @update:model-value="onDateChange"
        />
        <button v-if="startDate || endDate" class="clear-btn" @click.stop="clearDate">✕</button>
      </div>

      <div class="input-box">
        <span class="icon">👤</span>
        <span class="value">성인 2명</span>
      </div>

      <button class="search-btn" @click="handleSearch">숙소 검색</button>
    </div>

    <div class="filter-bar">
      <label><input type="checkbox" /> 무료취소</label>
      <button class="pill-btn" @click="openSortModal = true">정렬: {{ getSortLabel(sort) }}</button>
      <button class="pill-btn" @click="openPriceModal = true">
        가격: {{ priceRange[0].toLocaleString() }} ~ {{ priceRange[1].toLocaleString() }}원
      </button>
      <button class="pill-btn">호텔성급</button>
    </div>
    </div>

    <!-- 정렬 모달 -->
<div v-if="openSortModal" class="modal" @click.self="openSortModal = false">
  <div class="modal-content">
    <div class="modal-header">
      <h3>정렬 방식</h3>
    </div>
    <div class="modal-body">
      <ul>
        <li v-for="option in sortOptions" :key="option.value">
          <label>
            <input type="radio" :value="option.value" v-model="sort" />
            {{ option.label }}
          </label>
        </li>
      </ul>
    </div>
  </div>
</div>

<!-- 가격 모달 -->
<div v-if="openPriceModal" class="modal" @click.self="openPriceModal = false">
  <div class="modal-content">
    <div class="modal-header">
      <h3>가격 설정</h3>
    </div>
    <div class="modal-body">
      <label>가격 범위</label>
      <div>{{ priceRange[0].toLocaleString() }}원 ~ {{ priceRange[1].toLocaleString() }}원</div>
      <input type="range" v-model="priceRange[0]" min="0" max="500000" step="10000" />
      <input type="range" v-model="priceRange[1]" min="0" max="500000" step="10000" />
    </div>
  </div>
</div>


    <div v-if="loading" class="loading-overlay">로딩 중...</div>

      <div v-show="products.length" class="product-list-wrapper">
        <div v-for="product in products" :key="product.id" class="product-item">
          <img :src="product.imageUrl || defaultImage" alt="썸네일" class="thumbnail" />
          <div class="info">
            <h3>{{ product.title }}</h3>
            <p class="desc">{{ product.description }}</p>
            <p class="price">{{ product.price.toLocaleString() }}원</p>
            <router-link :to="`/products/${product.id}`">
              <button class="detail-btn">상세 보기</button>
            </router-link>
          </div>
        </div>
      </div>
      <p v-show="!products.length">검색 결과가 없습니다.</p>
    <div ref="scrollTrigger" style="height: 10px; visibility: hidden;"></div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import ko from 'date-fns/locale/ko'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

const page = ref(0);
const size = 10;
const hasMore = ref(true);

const route = useRoute()
const router = useRouter()

const searchValue = ref(route.query.searchValue || '')
const sort = ref(route.query.sort || 'startDate,desc')
const startDate = ref(route.query.startDate || '')
const endDate = ref(route.query.endDate || '')
const priceRange = ref([0, 500000])
const openSortModal = ref(false)
const openPriceModal = ref(false)
const openDateModal = ref(false)
const today = new Date()
const tomorrow = new Date()
tomorrow.setDate(today.getDate() + 1)

window.addEventListener('resize', () => {
  const header = document.querySelector('.header-wrapper')
  if (header && wrapperRef.value) {
    const headerHeight = header.offsetHeight
    wrapperRef.value.style.top = `${headerHeight}px`
  }
})

const dateRange = ref([
  new Date(startDate.value || today),
  new Date(endDate.value || tomorrow),
])

const onDateChange = (value) => {
  if (value && value.length === 2) {
    startDate.value = value[0].toISOString().slice(0, 10)
    endDate.value = value[1].toISOString().slice(0, 10)
    handleSearch()
  }
}

const applyDate = () => {
  if (dateRange.value[0] && dateRange.value[1]) {
    startDate.value = dateRange.value[0].toISOString().slice(0, 10)
    endDate.value = dateRange.value[1].toISOString().slice(0, 10)
  }
  openDateModal.value = false
  handleSearch()
}

const clearDate = () => {
  startDate.value = ''
  endDate.value = ''
  dateRange.value = [null, null]
}

const sortOptions = [
  { label: '최신순', value: 'startDate,desc' },
  { label: '낮은 가격순', value: 'price,asc' },
  { label: '높은 가격순', value: 'price,desc' },
  { label: '할인 높은순', value: 'discount,desc' }
]

const getSortLabel = (value) => {
  const option = sortOptions.find((o) => o.value === value)
  return option ? option.label : '정렬'
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const day = ['일', '월', '화', '수', '목', '금', '토'][d.getDay()]
  return `${d.getMonth() + 1}월 ${d.getDate()}일 (${day})`
}

const products = ref([])
const loading = ref(false)
const defaultImage = '/no-image.jpg'

const fetchProducts = async () => {
  if(!hasMore.value || loading.value) return

  loading.value = true

  try {
    const res = await axios.get('/api/product', {
      params: {
        page: page.value,
        size,
        searchKey: 'titleAndDescription',
        searchValue: searchValue.value,
        sort: sort.value,
        startDate: startDate.value,
        endDate: endDate.value,
        minPrice: priceRange.value[0],
        maxPrice: priceRange.value[1],
      },
    })

    const data = res.data;

    if (page.value === 1 && data.content.length > 0)
      products.value.splice(0, products.value.length, ...data.content) // 교체
    else
      products.value.push(...data.content); // 이어붙이기

    hasMore.value = !data.last;

    if (!data.last)
      page.value += 1;
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  hasMore.value = true

  router.replace({
    query: {
      searchValue: searchValue.value,
      sort: sort.value,
      startDate: startDate.value,
      endDate: endDate.value,
    },
  })

  fetchProducts();
}

const scrollTrigger = ref(null);
let observer = null;
const wrapperRef = ref(null);

onMounted(() => {
  page.value = 1;
  hasMore.value = true;

  fetchProducts();

  observer = new IntersectionObserver((entries) => {
    const entry = entries[0];

    if(entry.isIntersecting && !loading.value && hasMore.value) {
      observer.unobserve(entry.target) // 중복 방지

      fetchProducts().then(() => {
        if(scrollTrigger.value && hasMore.value)
          observer.observe(scrollTrigger.value); // 다시 감지
      })
    }
  }, {
    threshold: 1
  })

  if(scrollTrigger.value)
    observer.observe(scrollTrigger.value);

  const header = document.querySelector('.header-wrapper')
  if (header && wrapperRef.value) {
    const headerHeight = header.offsetHeight
    wrapperRef.value.style.top = `${headerHeight}px`
  }
})

onUnmounted(() => {
  if(observer && scrollTrigger.value)
    observer.unobserve(scrollTrigger.value);
})

watch(
  () => [searchValue.value, sort.value, startDate.value, endDate.value, priceRange.value[0], priceRange.value[1]],
  () => {
    handleSearch();
  }
)

watch(
  () => sort.value,
  async (newSort) => {
    await router.replace({
      query: {
        ...route.query,
        sort: newSort,
      },
    })

    router.go(0)
  }
)
</script>

<style scoped>
@import '@vuepic/vue-datepicker/dist/main.css';

.product-list {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}

.top-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  background: #f8f8f8;
  padding: 16px;
  border-radius: 12px;
  align-items: center;
  margin-bottom: 16px;
}

.input-box {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 6px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 999px;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
}

.search-input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  padding: 0;
  width: 100%;
}

.icon {
  font-size: 16px;
}

.clear-btn {
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
}

.search-btn {
  background: black;
  color: white;
  padding: 10px 18px;
  border-radius: 12px;
  font-weight: bold;
  border: none;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
  cursor: pointer;
}

.filter-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.pill-btn {
  border-radius: 999px;
  border: 1px solid #ccc;
  background: white;
  padding: 6px 12px;
  font-size: 14px;
  cursor: pointer;
}

.modal {
  position: fixed;
  top: 0; left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 20px;
  width: 400px;
  max-height: 90vh;
  overflow-y: visible;
  position: relative;
}

.calendar-header {
  font-weight: bold;
  margin-bottom: 12px;
  font-size: 16px;
}

.calendar-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.apply-btn {
  background: black;
  color: white;
  padding: 6px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

.product-list-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #fafafa;
}

.thumbnail {
  width: 160px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
}

.info {
  flex: 1;
}

.desc {
  margin: 8px 0;
  color: #666;
}

.price {
  font-weight: bold;
  margin-bottom: 10px;
}

.detail-btn {
  background-color: #c8ad7f;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.product-list-wrapper {
  min-height: 400px;
}

.loading-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(255, 255, 255, 0.6);
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
}
</style>
