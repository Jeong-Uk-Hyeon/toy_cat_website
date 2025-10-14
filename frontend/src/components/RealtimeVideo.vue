<template>
  <div>
    <h1>실시간 영상</h1>
    <img :src="videoUrl" alt="Live Camera Feed" width="640" height="480">

    <button @click="startPlay">놀이 시작</button>
    <button @click="endPlay">놀이 끝</button>
    <p>운동량: {{ exerciseAmount }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

//const videoUrl = "http://10.99.89.98:5000/video_feed";
const exerciseAmount = ref(0);

const startPlay = async () => {
  await axios.post("http://localhost:8000/api/laser/start");
};

const endPlay = async () => {
  const res = await axios.post("http://localhost:8000/api/laser/end");
  exerciseAmount.value = res.data.exerciseAmount;
};
</script>