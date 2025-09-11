<template>
	<div class="logout-bar">
		<button @click="logout">로그아웃</button>
	</div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  function logout() {
	// 로그아웃 로직 (예: localStorage.clear() 등) 필요 시 추가
	router.push('/') // LoginView로 이동
  }
  </script>
  
  <style scoped>
  .logout-bar {
	background-color: #e0e0e0;
	padding: 10px 20px;
	display: flex;
	align-items: center;
  }
  button {
	font-size: 14px;
	padding: 8px 12px;
	cursor: pointer;
  }
  </style>
  