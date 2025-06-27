<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>여행 챗봇</h2>
    </div>
    
    <div class="chat-messages">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.role]"
      >
        <div class="bubble">{{ msg.content }}</div>
      </div>
    </div>

    <div class="chat-input">
      <input
        v-model="userInput"
        type="text"
        placeholder="메시지를 입력하세요..."
        @keyup.enter="sendMessage"
      />
      <button @click="sendMessage">전송</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

//봇의 기본 시작 멘트 
const messages = ref([
  { role: 'model', content: ' 안녕하세요, 어디로 떠나고 싶으신가요? \n 여러분의 일정을 짜드립니다! \n (어디를, 누구와, 얼마나 함께할까요?)' }
])

//사용자의 입력
const userInput = ref('')

const sendMessage = async() => {
  const input = userInput.value.trim()
  if (!input) return

  // 사용자 메시지 Vue에 추가
  messages.value.push({ role: 'user', content: input })
  // 입력창 초기화
  userInput.value = ''

  const requestBody ={
    userInput : input,
    chatRequest: {
        contents: messages.value.map(msg=>({
            role: msg.role === 'user'?'user':'model',
            parts: [{text: msg.content}]


        }))
    }
    
  }

  
  try{
    const response = await axios.post(
        'http://localhost:8080/api/chat',
        requestBody
    )

    //const modelReply = response.data.contents.at(-1).parts[0] 
    const lastMessage = response.data.contents.at(-1)
const modelReplyText = lastMessage?.parts?.[0]?.text || '응답이 없어요. 다시 시도해주세요.'

messages.value.push({ role: 'model', content: modelReplyText })



    //messages.value.push({ role: 'model', content: modelReply })
  } catch (error) {
    console.error('챗봇 응답 실패:', error)
    messages.value.push({
      role: 'model',
      content: '서버 연결에 실패했어요. 다시 시도해주세요.'
    })
  }

}
</script>

<style scoped>
.chat-container {
  max-width: 600px;
  margin: 30px auto;
  border: 1px solid #ccc;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  height: 80vh;
  font-family: 'Segoe UI', sans-serif;
}

.chat-header {
  background-color: #3e8ed0;
  color: white;
  padding: 1rem;
  border-top-left-radius: 15px;
  border-top-right-radius: 15px;
  text-align: center;
}

.chat-messages {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
  background: #f7f7f7;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message {
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message.bot {
  justify-content: flex-start;
}

.bubble {
  white-space: pre-line;
  max-width: 60%;
  padding: 0.75rem 1rem;
  border-radius: 15px;
  background-color: #e0e0e0;
  word-break: break-word;
}

.message.user .bubble {
  background-color: #3e8ed0;
  color: white;
}

.chat-input {
  display: flex;
  padding: 1rem;
  border-top: 1px solid #ccc;
  background: white;
}

.chat-input input {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-right: 0.5rem;
}

.chat-input button {
  padding: 0.75rem 1rem;
  background-color: #3e8ed0;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}
</style>
