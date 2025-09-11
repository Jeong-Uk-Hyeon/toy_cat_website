//ExerciseData 엔티티와 서비스를 연결해주는 repository
package com.example.capston_system.repository;

import com.example.capston_system.entity.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Long> {
    List<ExerciseData> findByExerciseDateBetween(LocalDate start, LocalDate end);
}
