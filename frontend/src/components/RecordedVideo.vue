<template>
  <div class="video-viewer">
    <h1>날짜 선택</h1>
    
    <ul class="date-list">
      <li 
        v-for="date in dates" 
        :key="date" 
        @click="selectDate(date)"
        :class="{ active: selectedDate === date }"
      >
        {{ date }}
      </li>
    </ul>

    <div v-if="selectedDate" class="video-list-container">
      <h2>{{ selectedDate }} 영상 목록 ({{ videos.length }}개)</h2>
      <ul class="video-list">
        <li 
          v-for="video in videos" 
          :key="video" 
          @click="playVideo(video)"
          :class="{ active: selectedFilename === video }"
        >
          {{ video }}
        </li>
      </ul>
      <p v-if="videos.length === 0">해당 날짜에 녹화된 파일이 없습니다.</p>
    </div>

    <div v-if="videoUrl" class="video-player">
      <h3>재생 중: {{ selectedFilename }}</h3>
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
import { ref, onMounted } from 'vue';
import axios from 'axios';

// --- 반응형 상태 ---
const dates = ref([]); // 날짜 폴더 목록
const selectedDate = ref(''); // 현재 선택된 날짜
const videos = ref([]); // 선택된 날짜의 비디오 파일 목록
const videoUrl = ref(''); // <video> 태그에 바인딩될 스트림 URL
const selectedFilename = ref(''); // 현재 재생 중인 파일명

// --- 1. 날짜 목록 가져오기 ---
onMounted(async () => {
  try {
    // Spring Boot API 호출: /api/video/dates
    const response = await axios.get('/api/video/dates');
    dates.value = response.data;
    if (dates.value.length === 0) {
        console.warn("녹화된 날짜 폴더를 찾을 수 없습니다.");
    }
  } catch (error) {
    console.error('날짜 목록을 가져오는 데 실패했습니다:', error);
  }
});

// --- 2. 날짜 선택 시 파일 목록 가져오기 ---
const selectDate = async (date) => {
  selectedDate.value = date;
  selectedFilename.value = ''; // 파일 선택 초기화
  videoUrl.value = ''; // 비디오 URL 초기화

  try {
    // Spring Boot API 호출: /api/video/files/{date}
    const response = await axios.get(`/api/video/files/${date}`);
    videos.value = response.data;
  } catch (error) {
    console.error(`날짜 ${date}의 비디오 목록을 가져오는 데 실패했습니다:`, error);
    videos.value = [];
  }
};

// --- 3. 비디오 파일 선택 시 재생 ---
const playVideo = (filename) => {
  selectedFilename.value = filename;
  // Spring Boot 스트림 엔드포인트 URL 생성
  // Vue는 이 URL로 요청을 보내고, Spring Boot가 Range Header를 처리하여 파일 스트리밍을 시작합니다.
  videoUrl.value = `/api/video/stream/${selectedDate.value}/${filename}`;
};
</script>

<style scoped>
.video-viewer {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

h1 { color: #060606; margin-bottom: 20px; }
h2 { color: #333; font-size: 1.2em; margin-top: 15px; }

.date-list, .video-list {
  list-style-type: none;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.date-list li, .video-list li {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  background-color: #f8f8f8;
}

.date-list li:hover, .video-list li:hover {
  background-color: #e0e0e0;
}

.date-list li.active, .video-list li.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.video-player {
  margin-top: 30px;
  border: 1px solid #ccc;
  padding: 15px;
  border-radius: 8px;
  text-align: center;
}

video {
  width: 100%;
  max-width: 640px;
  height: auto;
  border-radius: 4px;
}
</style>