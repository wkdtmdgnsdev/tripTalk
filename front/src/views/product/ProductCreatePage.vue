<template>
  <div class="max-w-2xl mx-auto">
    <h1 class="text-2xl font-bold mb-4">상품 등록</h1>
    <ProductForm :onSubmitForm="handleSubmit" />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import axios from 'axios'
import ProductForm from '@/components/product/ProductForm.vue'

const router = useRouter()

const handleSubmit = async (form) => {
  try {
    const res = await axios.post('/api/product', form)
    alert('등록 성공!')
    router.push(`/products/${res.data}`)
  } catch (err) {
    alert('등록 실패!')
    console.error(err)
  }
}
</script>
