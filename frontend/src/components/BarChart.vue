<template>
  <div class="bar-chart-container">
    <div v-for="item in data" :key="item.label" class="bar-item" @click="$emit('bar-click', item)">
      <div class="label">{{ item.label }}</div>
      <div class="bar-wrapper">
        <div 
          class="bar" 
          :style="{ width: calculateBarWidth(item.value) + '%' }"
          :title="`${item.value} m`"
        >
          {{ item.value }}
        </div>
      </div>
    </div>
    <div class="legend">
      <span>최대 운동량: {{ maxValue }} m</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  data: {
    type: Array,
    required: true
  },
  // AmountExercise.vue에서 전달받은 기간 내 최대 운동량 값
  maxValue: {
    type: Number,
    default: 0
  }
});

defineEmits(['bar-click']);

// --- 핵심: 막대 길이를 동적으로 계산 ---
const calculateBarWidth = (currentValue) => {
  if (props.maxValue === 0) return 0;
  
  // 현재 값 / 기간 내 최대 값 * 100 = 상대적인 길이 비율
  const ratio = (currentValue / props.maxValue) * 100;
  
  // 최소 길이가 너무 짧아지지 않도록 1%를 최소값으로 설정 (선택 사항)
  return Math.max(1, ratio); 
};
</script>

<style scoped>
.bar-chart-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 90%;
  max-width: 800px;
  margin: 30px auto;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 6px;
}

.bar-item {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.label {
  width: 100px; /* 라벨 너비 고정 */
  font-weight: bold;
  text-align: right;
  padding-right: 10px;
}

.bar-wrapper {
  flex-grow: 1; /* 나머지 공간을 모두 차지 */
  height: 20px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.bar {
  height: 100%;
  background-color: #4CAF50; /* 막대 색상 */
  color: white;
  text-align: right;
  padding-right: 5px;
  box-sizing: border-box;
  font-size: 12px;
  line-height: 20px;
  transition: width 0.5s ease;
}

.legend {
    margin-top: 10px;
    font-size: 0.9em;
    color: #666;
    text-align: left;
    border-top: 1px dashed #ddd;
    padding-top: 10px;
}
</style>
