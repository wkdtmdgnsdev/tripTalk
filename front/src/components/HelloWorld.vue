<template>
  <div
    class="swiper-background"
    :style="{ background: currentBackground, transition: 'background 0.5s ease' }"
  >
    <div class="swiper-inner">
      <Swiper
        :slides-per-view="1"
        :space-between="30"
        :loop="true"
        class="mySwiper"
        @slideChange="onSlideChange"
      >
        <SwiperSlide v-for="(img, index) in images" :key="index">
          <img :src="img" class="slide-image" />
        </SwiperSlide>
      </Swiper>
    </div>
  </div>
</template>

<script setup>
import 'swiper/css'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { ref } from 'vue'

const images = [
  new URL('@/assets/busan.jpg', import.meta.url).href,
  new URL('@/assets/daejeon.jpg', import.meta.url).href,
  new URL('@/assets/seoul.jpg', import.meta.url).href
]

const backgrounds = [
  'linear-gradient(135deg, #d0e8ff, #a0c4ff)',
  'linear-gradient(135deg, #ffe29a, #ff8a65)',
  'linear-gradient(135deg, #d4fc79, #96e6a1)'
]

const currentBackground = ref(backgrounds[0])

function onSlideChange(swiper) {
  const realIndex = swiper.realIndex
  currentBackground.value = backgrounds[realIndex % backgrounds.length]
}
</script>

<style scoped>
/* 📌 배경 전체를 감싸는 div */
.swiper-background {
  width: 100%;
  min-height: 60vh;
  padding: 50px 20px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;   /* ✅ 수평 중앙 정렬 */
  align-items: center;       /* ✅ 수직 중앙 정렬 */
  background: linear-gradient(135deg, #d0e8ff, #a0c4ff); /* 기본 배경 예시 */
}

/* 📌 가운데 사진을 감싸는 영역 */
.swiper-inner {
  width: 50%;
  height: 50%;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;

  /* ✅ 위치 조정용 */
  position: relative;
  margin-left: auto;    /* 오른쪽으로 붙임 */
  margin-top: auto;     /* 아래쪽으로 붙임 */
  transform: translate(-15%, 20%);
}
/* 📌 이미지 크기 유지 */
.slide-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  display: block;
}
</style>