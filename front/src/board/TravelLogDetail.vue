<template>
  <div class="travel-log-detail">
    <h2>게시글 상세 페이지</h2>
    <p>글 ID: {{ id }}</p>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
const id = route.params.id
</script>
