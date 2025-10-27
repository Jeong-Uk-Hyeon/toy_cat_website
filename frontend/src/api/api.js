import axios from 'axios';

export const fetchExerciseData = (year, month, day = null) => {
    const params = { year, month };
    if (day !== null) params.day = day;

    return axios.get('/api/exercise', { params });
};

export const fetchExerciseDataByPeriod = async (startDate, endDate) => {
    // Spring Boot의 GET /api/exercise/period 엔드포인트에 요청을 보냅니다.
    const response = await axios.get('/api/exercise/period', {
        // startDate와 endDate를 쿼리 파라미터로 설정하여 전송
        params: {
            startDate: startDate,
            endDate: endDate
        }
    });
    
    // axios의 응답 객체 전체를 호출자(AmountExercise.vue의 search 함수)에게 반환합니다.
    return response;
}