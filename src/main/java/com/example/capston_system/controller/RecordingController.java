// 라즈베리파이로 부터 받은 녹화영상 수신 및 저장
package com.example.capston_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recording")
public class RecordingController {
    // 1. 설정값 주입 (파일 저장 위치)
    // application.properties 파일에 정의된 recordings.storage.path 값을 자동으로 가져옴
    @Value("${recordings.storage.path}")
    private String storagePath;
    // 2. API 엔드포인트 정의
    // 이 메서드는 외부(라즈베리파이)에서 HTTP POST 요청을 '/api/exercise/uploadRecording' 경로로 보낼 때 작동
    // consumes = "multipart/form-data": 파일 전송에 사용되는 특별한 형식(POST)만 받도록 설정합니다.
    @PostMapping(value = "/uploadRecording", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadRecording(@RequestParam("file") MultipartFile file) {
        // 3. 응답 객체 준비
        // 클라이언트에게 반환할 상태 메시지(JSON 형태)를 담을 Map 객체를 만듭니다.
        Map<String, String> resp = new HashMap<>();
        // 4. 요청 유효성 검사 (파일이 제대로 왔는지 확인)
        if (file == null || file.isEmpty()) {
            resp.put("status", "error");
            resp.put("message", "No file uploaded");
            return ResponseEntity.badRequest().body(resp);
        }

        // 5. 파일명 정리 (보안 강화)
        // 파일명에서 경로 구분 문자(/, \)와 같은 불필요하거나 악의적인 문자를 제거하여 안전한 파일명을 만듭니다.
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // 6. 저장 경로 설정
        // 저장 폴더: storagePath/yyyy-MM-dd/
        LocalDate today = LocalDate.now();
        // 최종 타겟 폴더 경로를 생성
        Path targetDir = Paths.get(storagePath, today.toString());

        try {
            // 7. 폴더 생성
            // 파일이 저장될 날짜별 폴더가 없으면 새로 만듬
            Files.createDirectories(targetDir);
            // 8. 최종 파일 저장 경로 확정 파일이름 까지 추가해서
            Path targetPath = targetDir.resolve(originalFilename);

            // 9. 파일 저장 실행 (핵심 로직)
            try {
                // file.getInputStream(): 업로드된 파일의 데이터를 읽어들이는 통로(스트림)입니다.
                // targetPath로 파일 데이터를 복사하여 저장합니다.
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // 파일 시스템 쓰기 오류 처리 (저장 도중 디스크 문제 등)
                resp.put("status", "error");
                resp.put("message", "Failed to save file: " + e.getMessage());
                return ResponseEntity.status(500).body(resp); // 500 Internal Server Error 반환
            }
            // 10. 성공 응답
            resp.put("status", "saved");
            // 저장된 파일의 절대 경로를 클라이언트에게 함께 반환합니다
            resp.put("savedPath", targetPath.toAbsolutePath().toString());
            // 200 OK 상태와 함께 성공 정보를 JSON으로 반환하고 메서드를 종료합니다.
            return ResponseEntity.ok(resp);

        } catch (IOException e) {
            // 디렉터리 생성 오류 처리 (상위 경로 권한 문제 등)
            resp.put("status", "error");
            resp.put("message", "Could not create directories: " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }
}


