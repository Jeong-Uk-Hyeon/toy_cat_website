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

    public int endPlay() {
        RestTemplate rest = new RestTemplate();
        Integer amount = rest.postForObject(raspberryPiUrl + "/end", null, Integer.class);

        if (amount == null) amount = 0;

        // 오늘 날짜
        LocalDate today = LocalDate.now();
        // 오늘 데이터가 있는지 확인하고 있으면 들고오고 없으면 0으로 새로 만들기
        ExerciseData data = repository.findByExerciseDate(today)
                .orElse(new ExerciseData(today, 0));

        data.setAmount(data.getAmount() + amount); // 누적 합산
        repository.save(data);

        return data.getAmount();
    }
}
