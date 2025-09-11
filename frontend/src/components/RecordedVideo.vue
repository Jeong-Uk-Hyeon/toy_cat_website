<template>
  <div>
    <h1>녹화 날짜 선택</h1>
    <ul>
      <li v-for="date in dates" :key="date" @click="selectDate(date)">
        {{ date }}
      </li>
    </ul>

    <div v-if="videos.length">
      <h3>{{ selectedDate }} 영상 목록</h3>
      <ul>
        <li v-for="video in videos" :key="video" @click="playVideo(video)">
          {{ video }}
        </li>
      </ul>
    </div>

    <div v-if="videoUrl">
      <!-- <video controls :src="videoUrl" width="640"></video> -->
      <video
        controls
        autoplay
        muted
        :src="videoUrl"
        width="640"
        style="background-color: black"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const dates = ref([])
const selectedDate = ref('')
const videos = ref([])
const videoUrl = ref('')

onMounted(async () => {
  const res = await axios.get('/api/dates')
  dates.value = res.data
})

const selectDate = async (date) => {
  selectedDate.value = date
  const res = await axios.get(`/api/videos/${date}`)
  videos.value = res.data
}

const playVideo = (filename) => {
  videoUrl.value = `/api/video-stream/${selectedDate.value}/${filename}`
}
</script> 