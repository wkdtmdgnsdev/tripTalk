<template>
  <div class="register-agree">
    <!-- 단계 표시 UI -->
    <div class="stepper">
      <div :class="['step', { active: currentStep === 1 }]">1. 약관 동의</div>
      <div :class="['step', { active: currentStep === 2 }]">2. 회원 정보 입력</div>
      <div :class="['step', { active: currentStep === 3 }]">3. 완료</div>
    </div>

    <h2>회원가입 약관 동의</h2>

    <form @submit.prevent="goNext">
      <div v-for="(term, idx) in terms" :key="term.id" class="term-item">
        <label>
          <input type="checkbox" v-model="term.checked" />
          <span class="term-title">{{ term.title }}</span>
        </label>
        <p class="term-content">{{ term.content }}</p>
      </div>

      <div class="agree-all">
        <label>
          <input
            type="checkbox"
            v-model="allAgree"
            @change="toggleAllAgree"
          />
          모두 동의합니다.
        </label>
      </div>

      <button
        type="submit"
        :disabled="!canProceed"
        class="next-btn"
      >
        다음으로
      </button>
    </form>
  </div>
</template>

<script setup>
import { reactive, computed, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const currentStep = 1

// 약관 목록 - 예시
const terms = reactive([
  {
    id: 1,
    title: '서비스 이용약관(필수)',
    content: '서비스 이용에 관한 약관 내용입니다. 대충 작성해도 됩니다.',
    checked: false,
  },
  {
    id: 2,
    title: '개인정보 처리방침(필수)',
    content: '개인정보 처리에 관한 방침 내용입니다. 대충 작성해도 됩니다.',
    checked: false,
  },
  {
    id: 3,
    title: '마케팅 정보 수신 동의(선택)',
    content: '마케팅 정보 수신에 관한 동의 내용입니다. 선택 사항입니다.',
    checked: false,
  },
])

const allAgree = ref(false)

// 전체 동의 체크박스 상태 변화에 따른 단일 체크박스 전체 선택/해제
function toggleAllAgree() {
  terms.forEach(term => {
    term.checked = allAgree.value
  })
}

// 단일 체크박스가 변할 때 전체 동의 체크박스 상태 동기화
watch(
  () => terms.map(term => term.checked),
  (newVals) => {
    allAgree.value = newVals.every(Boolean)
  },
  { deep: true }
)

// 모두 동의 (필수 항목만 체크했을 때) 가능 여부
const canProceed = computed(() => {
  // 필수 항목은 id 1, 2번으로 가정 (마케팅 동의는 선택)
  return terms
    .filter(term => term.id !== 3)
    .every(term => term.checked)
})

const router = useRouter()
// 다음 버튼 클릭 시
function goNext() {
  if (!canProceed.value) return

  router.push('/register/info')
}
</script>

<style scoped>
.register-agree {
  max-width: 480px;
  margin: 40px auto;
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-family: 'Noto Sans KR', sans-serif;
}

.stepper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.step {
  flex: 1;
  text-align: center;
  font-weight: 600;
  color: #999;
  padding-bottom: 8px;
  border-bottom: 2px solid #ddd;
  user-select: none;
}

.step.active {
  color: #c8ad7f; /* 베이지톤 강조색 */
  border-color: #c8ad7f;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
}

.term-item {
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 12px 16px;
  background: #faf7f2;
}

.term-item label {
  font-weight: 600;
  cursor: pointer;
  user-select: none;
}

.term-content {
  margin-top: 8px;
  font-size: 14px;
  color: #555;
  white-space: pre-wrap;
}

.agree-all {
  margin-top: 24px;
  padding: 12px 16px;
  border-top: 1px solid #ccc;
  background: #f5f0e6;
  font-weight: 600;
}

.agree-all label {
  cursor: pointer;
  user-select: none;
}

.next-btn {
  margin-top: 30px;
  width: 100%;
  padding: 14px 0;
  background-color: #c8ad7f; /* 베이지톤 */
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 700;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.25s ease;
}

.next-btn:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}
</style>
