<template>
  <div class="main-container">
    <div class="content-container">
      <LeftMenuView @change-view="currentView = $event" />
      <component :is="currentViewComponent" class="main-content" />
    </div>
    <LogoutView/>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import LogoutView from '../components/LogoutView.vue'
import LeftMenuView from '../components/LeftMenuView.vue'
import RealtimeVideo from '../components/RealtimeVideo.vue'
import RecordedVideo from '../components/RecordedVideo.vue'
import AmountExercise from '../components/AmountExercise.vue'
import RangeSetting from '../components/RangeSetting.vue'

const currentView = ref('CameraRealtime')

const currentViewComponent = computed(() => {
  switch (currentView.value) {
    case 'RecordedVideo': return RecordedVideo
    case 'AmountExercise': return AmountExercise
    case 'RangeSetting': return RangeSetting
    default: return RealtimeVideo
  }
})
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.content-container {
  display: flex;
  flex: 1;
}
.main-content {
  flex: 3;
  padding: 20px;
}
</style>
