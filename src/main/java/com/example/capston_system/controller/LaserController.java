// 레이저 on off를 컨트롤
package com.example.capston_system.controller;

import com.example.capston_system.service.ExerciseDataService;
import com.example.capston_system.service.LaserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/laser")
public class LaserController {
    @Autowired
    private LaserService laserService;

    private final ExerciseDataService exerciseDataService;

    // 생성자를 통해 의존성 주입 (가장 권장되는 방식)
    // Spring이 자동으로 ExerciseDataService의 인스턴스를 찾아서 연결해 줍니다.
    public LaserController(ExerciseDataService exerciseDataService) {
        this.exerciseDataService = exerciseDataService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startPlay() {
        laserService.startPlay();
        return ResponseEntity.ok("놀이 시작");
    }

    @PostMapping("/end")
    public ResponseEntity<Map<String, Integer>> endPlay() {
        laserService.endPlay();
        exerciseDataService.fetchAndSaveFromRaspberry();
        Map<String, Integer> response = new HashMap<>();
        int amount = 2000; //임시값
        response.put("운동량", amount);
        return ResponseEntity.ok(response);
    }
}
