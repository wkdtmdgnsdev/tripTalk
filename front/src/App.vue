<template>
  <div id="app">
    <Header /> 

    <main>
    <router-view />
    </main>

    <Footer />

    
  </div>
  </template>

<script setup>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

</script>

<style>
/* 전역 스타일 정의 가능 */
body {
  margin: 0;
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
}
</style>
