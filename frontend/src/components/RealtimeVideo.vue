<template>
  <div>
    <h1>실시간 영상</h1>
    <img :src="videoUrl" alt="Live Camera Feed" width="640" height="480">
  </div>
</template>

<script>
export default {
  data() {
    return {
      videoUrl: "http://10.246.165.98:5000/video_feed"  // Flask 서버 주소
    };
  }
};
</script>