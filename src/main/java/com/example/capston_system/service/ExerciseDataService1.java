//운동데이터를 날짜 범위 기준으로 검색하는 코드

package com.example.capston_system.service;

import com.example.capston_system.entity.ExerciseData;
import com.example.capston_system.repository.ExerciseDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExerciseDataService1 {
    @Autowired
    private ExerciseDataRepository repository;

    public List<ExerciseData> getByDateRange(LocalDate start, LocalDate end) {
        return repository.findByExerciseDateBetween(start, end);
    }
}
