import axios from 'axios';

export const fetchExerciseData = (year, month, day = null) => {
    const params = { year, month };
    if (day !== null) params.day = day;

    return axios.get('/api/exercise', { params });
};