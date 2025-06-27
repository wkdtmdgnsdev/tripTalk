<template>
  <form @submit.prevent="onSubmit" class="space-y-4">
    <div>
      <label>제목</label>
      <input v-model="form.title" required class="input" />
    </div>

    <div>
      <label>설명</label>
      <input v-model="form.description" required class="input" />
    </div>

    <div>
      <label>가격</label>
      <input v-model.number="form.price" type="number" required class="input" />
    </div>

    <div>
      <label>주소</label>
      <input v-model="form.address" required class="input" />
    </div>

    <div>
      <label>판매자 ID</label>
      <input v-model="form.sellerId" type="number" required class="input" />
    </div>

    <div>
      <label>카테고리 ID</label>
      <input v-model="form.categoryId" type="number" required class="input" />
    </div>

    <!-- 할인 정보 -->
    <div>
      <label>할인 설명</label>
      <input v-model="form.discount.description" class="input" />
    </div>

    <div>
      <label>할인 금액 (정액할인)</label>
      <input v-model.number="form.discount.amount" type="number" class="input" />
    </div>

    <div>
      <label>할인 비율 (0.2 = 20%)</label>
      <input v-model.number="form.discount.rate" type="number" class="input" />
    </div>

    <div>
      <label>여행 시작일</label>
      <input v-model="form.startDate" type="datetime-local" class="input" />
    </div>

    <div>
      <label>여행 종료일</label>
      <input v-model="form.endDate" type="datetime-local" class="input" />
    </div>

    <button type="submit" class="btn">
      {{ isEdit ? '수정' : '등록' }}
    </button>
  </form>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  initialValue: Object,
  isEdit: Boolean,
  onSubmitForm: Function,
})

const form = ref({
  title: '',
  description: '',
  price: 0,
  address: '',
  startDate: '',
  endDate: '',
  sellerId: '',
  categoryId: '',
  discount: {
    type: 'FIXED',
    amount: 0,
    rate: 0,
    description: '',
    durationDays: 0,
  },
})

onMounted(() => {
  if (props.initialValue) {
    Object.assign(form.value, props.initialValue)
  }
})

const onSubmit = () => {
  props.onSubmitForm?.(form.value)
}
</script>

<style scoped>
.input {
  display: block;
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn {
  background-color: #2d6cdf;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.btn:hover {
  background-color: #1b4faf;
}
</style>
