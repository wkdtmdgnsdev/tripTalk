<template>
  <div class="max-w-2xl mx-auto">
    <h1 class="text-2xl font-bold mb-4">상품 수정</h1>

    <ProductForm
      v-if="product"
      :initialValue="product"
      :isEdit="true"
      :onSubmitForm="handleUpdate"
    />
    <LoadingSpinner v-else />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import ProductForm from '@/components/product/ProductForm.vue'

const route = useRoute()
const router = useRouter()
const product = ref(null)

onMounted(async () => {
  const id = route.params.id
  const res = await axios.get(`/api/product/${id}`)
  product.value = res.data
})

const handleUpdate = async (form) => {
  try {
    await axios.put(`/api/product/${route.params.id}`, form)
    alert('수정 성공!')
    router.push(`/products/${route.params.id}`)
  } catch (err) {
    alert('수정 실패!')
    console.error(err)
  }
}
</script>
