<template>
  <div>
    <h1>놀이 범위 설정</h1>
    <p class="guide-text">
      **방법:** 영상 화면을 클릭하여 놀이 범위의 꼭짓점을 순서대로 지정하세요.
      완료 버튼을 누르면 설정된 좌표가 서버로 전송됩니다.
    </p>
    
    <div class="image-container">
      <img
        ref="img"
        src="http://10.126.61.98:5000/video_feed"
        alt="실시간 영상"
        width="640"
        height="480"
      />
      <canvas ref="canvas" @click="handleClick"></canvas>
    </div>

    <div class="control-panel">
      <button @click="sendPoints" :disabled="points.length < 2" class="btn-primary">
        완료 (좌표 전송)
      </button>
      <button @click="resetDrawing" class="btn-secondary">
        초기화
      </button>
      <p v-if="message" :class="message.type">{{ message.text }}</p>
    </div>

    <div class="debug-info">
      <h3>저장된 좌표 ({{ points.length }}개)</h3>
      <ul>
        <li v-for="(p, index) in points" :key="index">
          {{ index + 1 }}: ({{ p.x.toFixed(2) }}, {{ p.y.toFixed(2) }})
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios'; // axios를 사용하려면 npm install axios가 필요합니다.

// --- 반응형 상태 선언 ---
const canvas = ref(null);
const img = ref(null);
const points = ref([]); // 사용자가 클릭한 모든 {x, y} 좌표를 저장
const message = ref(null); // 사용자에게 표시할 메시지

// --- 로직: 캔버스 크기 설정 ---
onMounted(() => {
  const c = canvas.value;
  const i = img.value;
  
  if (i) {
    // 이미지 로드 완료 후 크기를 캔버스에 반영
    i.onload = () => {
      c.width = i.width;
      c.height = i.height;
      drawPolygon(); // 초기화 후 혹시 모를 캔버스 초기화를 위해 호출
    };
    // 이미 로드가 완료된 상태일 경우 (캐싱 등으로 인해)
    if (i.complete) {
      c.width = i.width;
      c.height = i.height;
      drawPolygon();
    }
  }
});

// --- 로직: 클릭 이벤트 핸들러 ---
const handleClick = (e) => {
  if (!canvas.value) return; // 캔버스가 없으면 실행 중단

  const rect = canvas.value.getBoundingClientRect();
  const x = e.clientX - rect.left; // 캔버스 기준 x 좌표
  const y = e.clientY - rect.top; // 캔버스 기준 y 좌표
  
  // 1. 좌표를 points 배열에 추가
  points.value.push({ x, y });
  
  // 2. 좌표를 콘솔에 출력 (요청 사항)
  console.log(`클릭 X: ${x.toFixed(2)}`);
  console.log(`클릭 Y: ${y.toFixed(2)}`);
  
  // 3. 캔버스에 점과 선 그리기
  drawPolygon();
};

// --- 로직: 캔버스에 다각형(점과 선) 그리기 ---
function drawPolygon() {
    const ctx = canvas.value.getContext('2d');
    ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);

    // ----------------------------------------------------
    // 1. 점 (Dot) 그리기 (채우기)
    // ----------------------------------------------------
    points.value.forEach((p) => {
        // 매번 새로운 경로를 시작하고 닫아, 선 그리기와 분리합니다.
        ctx.beginPath(); 
        ctx.fillStyle = 'red';
        ctx.arc(p.x, p.y, 4, 0, Math.PI * 2); // 원 경로 추가
        ctx.fill(); // 원만 채우고 이 경로는 여기서 끝납니다.
    });

    // ----------------------------------------------------
    // 2. 선 (Line) 그리기 (테두리)
    // ----------------------------------------------------
    if (points.value.length > 1) {
        ctx.strokeStyle = 'red';
        ctx.lineWidth = 2;
        
        ctx.beginPath();
        
        // 첫 번째 점으로 이동
        ctx.moveTo(points.value[0].x, points.value[0].y); 
        
        // 두 번째 점부터 끝까지 연결
        for (let i = 0; i < points.value.length; i++) {
            ctx.lineTo(points.value[i].x, points.value[i].y);
        }
        
        // ctx.closePath(); // 다각형 닫는 선을 원치 않으면 이 부분은 여전히 주석 유지
        ctx.stroke(); // 선 표시
    }
}

// --- 기능: 초기화 버튼 ---
const resetDrawing = () => {
  // 1. points 배열 초기화 (요청 사항)
  points.value = [];
  
  // 2. 캔버스 그림 지우기 (요청 사항)
  drawPolygon(); // 빈 배열로 호출하면 clearRect만 실행됨

  message.value = { text: '초기화 완료. 다시 범위를 지정해주세요.', type: 'info' };
  console.log('--- 초기화 완료: points 배열 비워짐 ---');
};

// --- 기능: 완료(좌표 전송) 버튼 ---
const sendPoints = async () => {
  if (points.value.length < 2) {
    message.value = { text: '최소 두 개 이상의 점을 클릭해야 합니다.', type: 'error' };
    return;
  }
  
  // 1. 전송할 데이터 준비
  const dataToSend = {
    // 서버가 이해할 수 있는 형식으로 데이터를 변환할 수 있습니다 (예: JSON 문자열, 배열)
    area_points: points.value.map(p => [p.x, p.y])
  };

  // 2. 전송한 값을 콘솔에 띄움 (요청 사항)
  console.log('--- 서버로 전송될 데이터 ---');
  console.log(dataToSend);
  
  // 3. 라즈베리파이(Spring Boot 프록시)로 데이터 전송
  try {
    // API 엔드포인트는 스프링부트의 컨트롤러 경로와 일치해야 합니다.
    const response = await axios.post('/api/setPlayArea', dataToSend); 
    
    if (response.data && response.data.success) {
      message.value = { text: '✅ 범위 설정 좌표 전송 성공!', type: 'success' };
    } else {
      message.value = { text: `❌ 전송 실패: ${response.data ? response.data.message : '서버 응답 오류'}`, type: 'error' };
    }

  } catch (error) {
    console.error('API 전송 오류:', error);
    message.value = { text: '❌ 서버 통신 오류 발생. 라즈베리파이 서버 상태를 확인하세요.', type: 'error' };
  }
};
</script>

<style scoped>
/* 안내 문구 스타일 */
.guide-text {
  font-size: 1.1em;
  color: #333;
  margin-bottom: 20px;
}
.guide-text strong {
  color: darkred;
}

/* 이미지 및 캔버스 겹침 */
.image-container {
  position: relative;
  display: inline-block;
  border: 2px solid #0056b3; /* 컨테이너 경계 표시 */
}

.image-container img {
  display: block;
}

.image-container canvas {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
  cursor: crosshair; /* 캔버스 위에서 마우스 커서 변경 */
}

/* 컨트롤 및 메시지 */
.control-panel {
  margin-top: 15px;
  display: flex;
  gap: 10px;
  align-items: center;
}

.btn-primary, .btn-secondary {
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.debug-info {
  margin-top: 20px;
  text-align: left;
  border-top: 1px dashed #ccc;
  padding-top: 10px;
}

/* 메시지 스타일 */
.info {
  color: #007bff;
}
.success {
  color: #4CAF50;
  font-weight: bold;
}
.error {
  color: darkred;
  font-weight: bold;
}
</style>

<!-- <template>
  <h1> 놀이범위 설정</h1>
  <div class="image-container">
    <img ref="img" src="http://10.126.61.98:5000/video_feed" alt="base image" width="640" height="480" />
    <canvas ref="canvas" @click="handleClick"></canvas>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'

const canvas = ref(null)
const img = ref(null)
const points = ref([]) 

const handleClick = async (e) => {
  const rect = canvas.value.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  console.log(x)
  console.log(y)
  points.value.push({ x, y })
  drawPolygon()
  
};

onMounted(() => {
  const c = canvas.value
  const i = img.value
  // 이미지 로드 완료 후 크기를 캔버스에 반영
  i.onload = () => {
    c.width = i.width
    c.height = i.height
  }
})
</script>

<style scoped>
.image-container {
  position: relative;
  display: inline-block;
}

.image-container img {
  display: block;
}

.image-container canvas {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10; /* 이미지보다 위로 */
}
</style> -->
