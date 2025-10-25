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
        // **[수정] String.class로 응답을 받아 text/plain 오류를 피합니다.**
        String amountString = rest.postForObject(raspberryPiUrl + "/end", null, String.class);

        Integer amount;
        try {
            // **[추가] 수동으로 문자열을 정수로 변환합니다.**
            amount = Integer.parseInt(amountString.trim());
        } catch (NumberFormatException e) {
            // 변환 실패 시 (예: 라즈베리파이가 이상한 문자열을 보낸 경우)
            System.err.println("ERROR: Failed to parse amount from Pi response: " + amountString);
            amount = 0;
        }
//        Integer amount = rest.postForObject(raspberryPiUrl + "/end", null, Integer.class);

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
