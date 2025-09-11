<template>
  <div class="chart">
    <div
      v-for="bar in data"
      :key="bar.label"
      class="bar-wrapper"
      @click="$emit('bar-click', bar)"
    >
      <div class="bar-value">{{ bar.value }}</div> <!-- 운동량 -->
      <div
        class="bar"
        :style="{ height: bar.value + 'px' }"
      ></div>
      <div class="bar-label">{{ bar.label.split('-')[2] }}일</div> <!-- 날짜 -->
    </div>
  </div>
</template>

<script setup>
defineProps(['data'])
</script>

<style scoped>
.chart {
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: flex-end;
}

.bar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bar {
  width: 20px;
  background: #4caf50;
  cursor: pointer;
}

.bar-value {
  margin-bottom: 5px;
  font-size: 14px;
  color: #333;
}

.bar-label {
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}
</style>
