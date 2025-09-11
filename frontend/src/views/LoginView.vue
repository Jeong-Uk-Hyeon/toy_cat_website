<template>
  <div class="login-container">
    <div class="logo-wrap">
      <img src="@/assets/Loginlogo.png" alt="로고" width="200" /> <!-- 이미지 파일 위치-->
    </div>

    <div class="form-wrap">
      <input
        type="text"
        placeholder="아이디"
        v-model="form.userid"
        class="input-box"
      /> <!-- 아이디 변수 이름은 form.userid --> 
      <input
        type="password"
        placeholder="비밀번호"
        v-model="form.userpass"
        class="input-box"
        @keyup.enter="submit"
      /> <!-- 비밀번호 변수 이름은 form.userpass -->
      <button @click="submit" class="login-button">Login</button> <!-- 버튼을 누르변 submit함수가 실행행-->
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue' //반응형 사용을 위해
import { useRouter } from 'vue-router' //라우터 이용을 위해
import axios from 'axios' // http 요청을 위해 별도로 설치 필요: npm install axios

const router = useRouter()

const form = reactive({ //form이라는 반응형 객체를 만듬 userid와 userpass를 받아 이 객체에 저장
  userid: '',
  userpass: ''
})
const submit = async () => {
  try {
    const response = await axios.post('http://localhost:8000/api/login', form)
    
    if (response.data.success) {
      router.push('/main') // MainView.vue로 이동
    } 
    else {
      alert('아이디 또는 비밀번호가 일치하지 않습니다.')
    }
  } 
  catch (error) {
    console.error(error)
    alert('서버 오류가 발생했습니다.')
  }
}
</script>

<style scoped>
.login-container {
  text-align: center;
  padding: 30px;
}
.input-box {
  display: block;
  margin: 10px auto;
  padding: 10px;
  width: 200px;
}
.login-button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>