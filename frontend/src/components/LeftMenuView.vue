<template>
	<div class="menu">
		<button @click="$emit('change-view', 'RealtimeVideo')">실시간 영상</button>
		<button @click="$emit('change-view', 'RecordedVideo')">녹화된 영상</button>
		<button @click="$emit('change-view', 'AmountExercise')">운동량</button>
		<button @click="$emit('change-view', 'RangeSetting')">놀이범위 설정</button>
	</div>
  </template>
  
  <script setup>
  </script>
  
  <style scoped>
  .menu {
	flex: 1;
	background-color: #f0f0f0;
	padding: 20px;
	display: flex;
	flex-direction: column;
	gap: 10px;
  }
  button {
	padding: 10px;
	font-size: 16px;
	cursor: pointer;
  }
  </style>