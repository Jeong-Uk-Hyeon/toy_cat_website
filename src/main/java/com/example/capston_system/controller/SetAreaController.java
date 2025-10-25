// 사용자가 지정한 범위 좌표값들을 라즈베리 파이로 전송
package com.example.capston_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class SetAreaController {

    // 연결할 Flask 서버 주소. (기존 코드에서 정의된 변수)
    @Value("${raspberrypi.url}")
    private String raspberryPiUrl;

    // HTTP 요청을 보내기 위한 RestTemplate 객체 (Spring에서 권장하는 클라이언트)
    private final RestTemplate restTemplate = new RestTemplate();

    // 🚀 [POST] 놀이 범위 좌표를 Flask 서버로 전송하는 프록시 메서드
    @PostMapping("/setPlayArea")
    public ResponseEntity<Map<String, Object>> setPlayArea(@RequestBody Map<String, Object> data) {

        // 1. Flask 서버의 타겟 URL 설정
        // Flask 서버의 엔드포인트가 '/set_area'
        String flaskUrl = raspberryPiUrl + "/set_area";

        // 2. Vue에서 받은 데이터 확인 (디버깅)
        System.out.println("=== 📐 놀이 범위 설정 요청 수신 ===");
        System.out.println("➡️ Vue에서 수신한 데이터: " + data);

        // 3. Flask 서버로 전달할 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 요청 엔티티 (데이터와 헤더) 생성
        // data 맵에는 Vue에서 보낸 'area_points'가 JSON 형식으로 들어있습니다.
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);

        try {
            // 5. Flask 서버로 POST 요청 중계 (프록시 역할)
            // 요청을 보내고 Flask 서버의 응답을 그대로 받습니다.
            ResponseEntity<Map> flaskResponse = restTemplate.exchange(
                    flaskUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // 6. Flask 서버 응답 처리
            System.out.println("⬅️ Flask 응답 코드: " + flaskResponse.getStatusCode());
            System.out.println("⬅️ Flask 응답 본문: " + flaskResponse.getBody());

            // Flask 서버의 응답을 Vue.js 클라이언트에게 그대로 반환
            return new ResponseEntity<>(flaskResponse.getBody(), flaskResponse.getStatusCode());

        } catch (Exception e) {
            System.err.println("❌ Flask 서버 통신 오류 (setPlayArea): " + e.getMessage());
            e.printStackTrace();

            // 통신 오류 발생 시 클라이언트에게 500 에러와 메시지 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Flask 서버와 통신 중 오류 발생: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- (기존의 getDates, getVideosByDate, proxyVideoStream 메서드들은 여기에 그대로 유지) ---
}
