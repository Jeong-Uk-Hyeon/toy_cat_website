<template>
  <div>
    <h1>실시간 영상</h1>
    <img :src="videoUrl" alt="Live Camera Feed" width="640" height="480">

    <h2 v-if="isPlaying" style="color: green;">
        현재 놀이 중입니다!
    </h2>

    <button @click="startPlay" :disabled="isPlaying">놀이 시작</button>
    <button @click="endPlay" :disabled="!isPlaying">놀이 끝</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const videoUrl = "http://192.168.0.27:5000/video_feed";
const exerciseAmount = ref(0);
// 2. 놀이 상태를 추적할 반응형 변수 추가
const isPlaying = ref(false); 

const startPlay = async () => {
  try {
    await axios.post("http://localhost:8000/api/laser/start");
    // 3. API 호출 성공 후 상태를 'true'로 변경
    isPlaying.value = true;
    console.log("놀이가 시작되었습니다.");
  } catch (error) {
    console.error("놀이 시작 API 호출 실패:", error);
  }
};

const endPlay = async () => {
  try {
    const res = await axios.post("http://localhost:8000/api/laser/end");
    exerciseAmount.value = res.data.exerciseAmount;
    // 4. API 호출 성공 후 상태를 'false'로 변경
    isPlaying.value = false;
    console.log("놀이가 종료되었습니다. 운동량:", exerciseAmount.value);
  } catch (error) {
    console.error("놀이 끝 API 호출 실패:", error);
  }
};
</script>