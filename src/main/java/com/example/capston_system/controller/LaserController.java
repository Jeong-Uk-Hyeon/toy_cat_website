// 레이저 on off를 컨트롤
package com.example.capston_system.controller;

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

    @PostMapping("/start")
    public ResponseEntity<String> startPlay() {
        laserService.startPlay();
        return ResponseEntity.ok("놀이 시작");
    }

    @PostMapping("/end")
    public ResponseEntity<Map<String, Integer>> endPlay() {
        int amount = laserService.endPlay();
        Map<String, Integer> response = new HashMap<>();
        response.put("운동량", amount);
        return ResponseEntity.ok(response);
    }
}
