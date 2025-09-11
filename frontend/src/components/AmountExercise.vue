<template>
  <div class="amount-exercise">
    <h1>운동량</h1>
    <div class="form-container">
      <select v-model="year">
        <option v-for="y in [2025]" :key="y" :value="y">{{ y }}</option>
      </select>
      <select v-model="month">
        <option v-for="m in 12" :key="m" :value="m">{{ m }}</option>
      </select>
      <input v-model.number="day" placeholder="날짜 (선택)" />
      <button @click="search">검색</button>
    </div>
    <BarChart :data="chartData" @bar-click="onBarClick" />
    <div v-if="selectedData">
      <h3>{{ selectedData.exerciseDate }} 운동량</h3>
      <p>{{ selectedData.amount }} m</p>
      <p>{{ (selectedData.amount * 0.01).toFixed(2) }} kcal</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { fetchExerciseData } from '@/api/api'
import BarChart from './BarChart.vue' // 커스텀 막대그래프 컴포넌트

const year = ref(2025)
const month = ref(6)
const day = ref(null)
const chartData = ref([])
const selectedData = ref(null)

const search = async () => {
  const { data } = await fetchExerciseData(year.value, month.value, day.value)
  chartData.value = data.map(item => ({
    label: item.exerciseDate,
    value: item.amount,
    full: item
  }))
  selectedData.value = null
}

const onBarClick = (data) => {
  selectedData.value = data.full
}
</script>

<style scoped>
.amount-exercise {
  width: 100%;
  height: 100%;

.form-container {
  margin-bottom: 20px; /* 👉 한 줄 정도 여백 */
  display: flex;
  gap: 10px; /* 검색 요소 사이 여백 */
  justify-content: center;
}

}
</style>