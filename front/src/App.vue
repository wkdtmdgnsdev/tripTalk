<template>
  <div id="app">
    <div class="header-wrapper" ref="headerRef">
      <Header /> 
    </div>

    <main>
    <router-view />
    </main>

    <Footer />

    
  </div>
  </template>

<script setup>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { ref } from 'vue';

const headerRef = ref(null);
</script>

<style>
/* 전역 스타일 정의 가능 */
body {
  margin: 0;
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
}

/* 스크롤시 헤더 고정 */
.header-wrapper {
  position: sticky;
  top: 0;
  z-index: 999;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
