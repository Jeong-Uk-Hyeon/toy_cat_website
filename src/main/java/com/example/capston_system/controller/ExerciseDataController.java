package com.example.capston_system.controller;

import com.example.capston_system.entity.ExerciseData;
import com.example.capston_system.service.ExerciseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseDataController {

    private final ExerciseDataService service;

    public ExerciseDataController(ExerciseDataService service) {
        this.service = service;
    }

    // ================== 범위에 따른 운동량 요청 =====================
    @GetMapping
    public List<ExerciseData> getByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Integer day
    ) {
        LocalDate start = LocalDate.of(year, month, day != null ? day : 1);
        LocalDate end = (day != null) ? start : start.withDayOfMonth(start.lengthOfMonth());
        return service.getByDateRange(start, end);
    }

    // =================== DB에 저장 기능 테스트 용 코드 =====================
    @PostMapping("/test-fetch")
    public ExerciseData testFetch() {
        return service.fetchAndSaveFromRaspberry();
    }
}

//@RestController
//@RequestMapping("/api/exercise")
//public class ExerciseDataController {
//    @Autowired
//    private ExerciseDataService service;
//
//    @GetMapping
//    public List<ExerciseData> getByMonth(
//            @RequestParam int year,
//            @RequestParam int month,
//            @RequestParam(required = false) Integer day
//    ) {
//        LocalDate start = LocalDate.of(year, month, day != null ? day : 1);
//        LocalDate end = day != null
//                ? start
//                : start.withDayOfMonth(start.lengthOfMonth());
//
//        return service.getByDateRange(start, end);
//    }
//}
