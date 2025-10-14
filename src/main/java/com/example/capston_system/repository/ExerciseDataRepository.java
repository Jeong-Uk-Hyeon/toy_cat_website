//ExerciseData 엔티티와 서비스를 연결해주는 repository
package com.example.capston_system.repository;

import com.example.capston_system.entity.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Long> {

    // 기존 기능: 기간 조회 (그대로 유지)
    List<ExerciseData> findByExerciseDateBetween(LocalDate start, LocalDate end);

    // 새로 추가: 특정 날짜의 엔티티를 Optional로 조회
    Optional<ExerciseData> findByExerciseDate(LocalDate date);
}

//package com.example.capston_system.repository;
//
//import com.example.capston_system.entity.ExerciseData;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Repository
//public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Long> {
//    List<ExerciseData> findByExerciseDateBetween(LocalDate start, LocalDate end);
//}
