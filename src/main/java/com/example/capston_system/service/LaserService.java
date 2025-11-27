// 레이저 온 오프시 기능들 실행하는 코드
package com.example.capston_system.service;

import com.example.capston_system.entity.ExerciseData;
import com.example.capston_system.repository.ExerciseDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class LaserService {
    @Autowired
    private ExerciseDataRepository repository;

    @Value("${raspberrypi.url}")
    private String raspberryPiUrl;

    public void startPlay() {
        RestTemplate rest = new RestTemplate();
        rest.postForObject(raspberryPiUrl + "/start", null, String.class);
    }

    public void endPlay() {
        RestTemplate rest = new RestTemplate();
        // **[수정] String.class로 응답을 받아 text/plain 오류를 피합니다.**
        String amountString = rest.postForObject(raspberryPiUrl + "/end", null, String.class);
    }
}
