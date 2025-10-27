<template>
  <div class="amount-exercise">
    <h1>운동량 조회</h1>
    
    <div class="form-container">
      <label for="start-date">시작일:</label>
      <input type="date" id="start-date" v-model="startDate" required />
      
      <label for="end-date">종료일:</label>
      <input type="date" id="end-date" v-model="endDate" required />
      
      <button @click="search" :disabled="!startDate || !endDate">검색</button>
    </div>
    
    <BarChart :data="chartData" :max-value="maxAmount" @bar-click="onBarClick" />
    
    <div v-if="selectedData">
      <h3>{{ selectedData.exerciseDate }} 운동량 상세</h3>
      <p>거리: {{ selectedData.amount }} m</p>
      <p>칼로리 소모 (추정): **{{ (selectedData.amount * 0.0037453).toFixed(2) }} kcal**</p>
      <p v-if="maxAmount > 0">
        최대 운동량 대비: {{ (selectedData.amount / maxAmount * 100).toFixed(1) }}%
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue' // computed 추가
import { fetchExerciseDataByPeriod } from '@/api/api' // API 함수명 변경
import BarChart from './BarChart.vue' // 커스텀 막대그래프 컴포넌트

// 현재 날짜를 기반으로 초기 시작일/종료일 설정
const today = new Date().toISOString().substring(0, 10);
const sevenDaysAgo = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString().substring(0, 10);

const startDate = ref(sevenDaysAgo); // 시작일 (YYYY-MM-DD 형식)
const endDate = ref(today);       // 종료일 (YYYY-MM-DD 형식)
const chartData = ref([]);
const selectedData = ref(null);

// 조회된 데이터에서 최대 운동량(Max Amount)을 계산하는 computed 속성
const maxAmount = computed(() => {
    // chartData.value 배열에서 value (amount) 값 중 최대값을 찾습니다.
    if (chartData.value.length === 0) return 0;
    
    const amounts = chartData.value.map(item => item.value);
    return Math.max(...amounts);
});

// --- 검색 및 데이터 처리 로직 ---
const search = async () => {
  // 1. 새로운 API 함수 호출 (시작일, 종료일 전달)
  try {
    const response = await fetchExerciseDataByPeriod(startDate.value, endDate.value);
    const rawData = response.data; 
    
    // 2. 응답 데이터를 차트 데이터 형식에 맞게 매핑
    chartData.value = rawData.map(item => ({
      label: item.exerciseDate, // x축 라벨 (예: "2025-06-01")
      value: item.amount,       // 실제 운동량 값 (막대그래프 높이)
      full: item                // 원본 데이터를 통째로 저장 (클릭 이벤트에 사용)
    }));
    
    // 3. 선택된 데이터 초기화 (새로운 검색이므로)
    selectedData.value = null; 

  } catch (error) {
    console.error("데이터 조회 중 오류 발생:", error);
    alert("데이터를 불러오는 데 실패했습니다. 서버를 확인해주세요.");
  }
};

// BarChart 컴포넌트에서 클릭 이벤트 발생 시 호출됨
const onBarClick = (data) => {
  selectedData.value = data.full;
};

// 컴포넌트 로드 시 초기 데이터 조회
search(); 
</script>

<style scoped>
.amount-exercise {
  width: 100%;
  height: 100%;
}

.form-container {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
}
.form-container input[type="date"] {
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
}
.form-container label {
    font-weight: bold;
    color: #333;
}
.form-container button {
    padding: 8px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
.form-container button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

</style>