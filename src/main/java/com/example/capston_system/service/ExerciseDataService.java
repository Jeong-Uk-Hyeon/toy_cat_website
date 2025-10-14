// 기능 1 범위정해서 운동량 조회
// 기능 2 DB에 운동량 저장
// 기능 3 매일 00:10분마다 실행

package com.example.capston_system.service; //이 코드의 위치

//필요한 클래스들 import

import com.example.capston_system.entity.ExerciseData;
import com.example.capston_system.repository.ExerciseDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseDataService {

    @Autowired
    private ExerciseDataRepository repository;

    //다른 서버에 HTTP 요청을 보내는 restTemplate 객체 생성 여기선 라즈베리에 GET요청을 보내서 값을 받아 옴
    private final RestTemplate restTemplate = new RestTemplate();
    // @value 어노테이션 ${}이라는 문법을 통해 application.properties에 정의된 raspberrypi.url 값을 찾고 밑에 변수에 값 넣어 줌
    @Value("${raspberrypi.url}")
    private String raspberryPiUrl;

    //DTO로 record 객체를 사용 record클래스를 사용하면 변수명 여기선 RaspberryResponse도 하나의 클래스가 됨
    public record RaspberryResponse(int amount) {}

    // 범위 정해서 조회

    public List<ExerciseData> getByDateRange(LocalDate start, LocalDate end) {
        return repository.findByExerciseDateBetween(start, end);
    }

    // 라즈베리 파이에서 운동량 값 받아와서 DB저장하고 저장한 엔티티 반환하는 메서드
    @Transactional
    public ExerciseData fetchAndSaveFromRaspberry() {
        // 1) 라즈베리에서 값 가져오기
        String url = raspberryPiUrl + "/gettotal";
        RaspberryResponse response = restTemplate.getForObject(url, RaspberryResponse.class);

        if (response == null) {
            throw new RuntimeException("라즈베리 응답 없음");
        }

        // 2) KST 기준 '어제' 날짜 계산
        LocalDate exerciseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        // 3) 같은 날짜의 레코드가 있는지 조회
        Optional<ExerciseData> opt = repository.findByExerciseDate(exerciseDate);

        ExerciseData saved;
        if (opt.isPresent()) {
            // 4-a) 있으면 기존 amount에 더해서 업데이트
            ExerciseData existing = opt.get();
            existing.setAmount(existing.getAmount() + response.amount());
            saved = repository.save(existing); // UPDATE
        } else {
            // 4-b) 없으면 새 엔티티 생성 후 저장
            ExerciseData data = new ExerciseData(exerciseDate, response.amount());
            saved = repository.save(data); // INSERT
        }

        // 5) 저장된 엔티티 반환
        return saved;
    }
//    중복날자시 새로 ID생성 해결 안된 버전
//    public ExerciseData fetchAndSaveFromRaspberry(){
//        String url = raspberryPiUrl + "/gettotal";
//        RaspberryResponse response = restTemplate.getForObject(url, RaspberryResponse.class);
//
//        if (response == null) {
//            throw new RuntimeException("라즈베리 응답 없음");
//        }
//
//        LocalDate exerciseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
//        //DB객체  생성
//        ExerciseData data = new ExerciseData();
//        data.setAmount(response.amount());
//        data.setExerciseDate(exerciseDate);
//    //repository.save(entity)는 entity DB에 저장하고 entity 반환
//        return repository.save(data);
//    }

    // ===================== 스케줄러 =====================
    //@Scheduled 스프링 기능으로 특정시간마다 자동으로 실행할 메서드에 붙임
//    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Seoul")
//    public void scheduledFetchAndSave() {
//        try {
//            ExerciseData saved = fetchAndSaveFromRaspberry();
//            System.out.println("[스케줄러] " + saved.getExerciseDate() + " 저장 완료");
//        } catch (Exception e) {
//            System.err.println("[스케줄러] 오류 발생: " + e.getMessage());
//        }
//    }
}

