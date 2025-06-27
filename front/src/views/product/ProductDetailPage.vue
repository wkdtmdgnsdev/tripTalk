<template>
  <div v-if="loading">
    <LoadingSpinner />
  </div>

  <div v-else-if="product">
    <h1 class="text-2xl font-bold">{{ product.title }}</h1>
    <p>{{ product.description }}</p>
    <p>가격: {{ product.price.toLocaleString() }}원</p>
    <p>주소: {{ product.address }}</p>
    <p>판매자: {{ product.sellerName }}</p>
    <p>카테고리: {{ product.categoryName }}</p>

    <div v-if="product.discount">
      <p class="text-red-500">할인 설명: {{ product.discount.description }}</p>
    </div>
  </div>

  <ErrorMessage v-else message="상품을 불러올 수 없습니다." />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const product = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await axios.get(`/api/product/${route.params.id}`)
    product.value = res.data
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
})
</script>
