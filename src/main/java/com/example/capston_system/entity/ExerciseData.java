// exercise_data 테이블 다루는 클래스 즉 exercise_data 엔티티

package com.example.capston_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "exercise_data") // DB 테이블명
public class ExerciseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB 컬럼명이 exercise_date 라면 명시적으로 매핑
    @Column(name = "exercise_date", nullable = false)
    private LocalDate exerciseDate;

    @Column(name = "amount", nullable = false)
    private int amount;

    public ExerciseData() {}

    // 생성자: (날짜, 양)
    public ExerciseData(LocalDate exerciseDate, int amount) {
        this.exerciseDate = exerciseDate;
        this.amount = amount;
    }

    // 이 엔티티의 메서드 들
    public Long getId() {
        return id;
    }

    public LocalDate getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(LocalDate exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}


//package com.example.capston_system.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "exercise_data")
//public class ExerciseData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ 이 부분 추가
//    private LocalDate exerciseDate;
//
//    private int amount;
//
//    // ✅ getter, setter 추가 (생략 가능)
//    public Long getId() {
//        return id;
//    }
//
//    public LocalDate getExerciseDate() {
//        return exerciseDate;
//    }
//    // 운동날짜 설정 메서드
//    public void setExerciseDate(LocalDate exerciseDate) {
//        this.exerciseDate = exerciseDate;
//    }
//    // 운동량 반환 메서드
//    public int getAmount() {
//        return amount;
//    }
//    // 운동량 설정 메서드
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//}