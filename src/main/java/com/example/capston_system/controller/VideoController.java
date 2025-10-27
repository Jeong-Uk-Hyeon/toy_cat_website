package com.example.capston_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    // application.properties 또는 환경 설정에서 정의된 녹화 파일의 기본 경로
    // 예: file.recording-base-path=../recordings/
    @Value("${file.recording-base-path}")
    private String videoBasePath;

    /**
     * 1. [GET /api/video/dates] 녹화된 날짜(폴더) 목록을 반환합니다.
     */
    @GetMapping("/dates")
    public ResponseEntity<List<String>> getRecordingDates() {
        try {
            // Path 객체를 사용하여 기본 경로 폴더에 접근
            Path baseDir = Paths.get(videoBasePath);

            // 폴더가 존재하지 않거나 읽을 수 없으면 빈 리스트 반환
            if (!Files.exists(baseDir) || !Files.isDirectory(baseDir)) {
                return ResponseEntity.ok(List.of());
            }

            // 폴더 목록을 읽고, 파일이 아닌 디렉토리만 필터링하여 리스트로 반환
            List<String> dates = Files.list(baseDir)
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .sorted((d1, d2) -> d2.compareTo(d1)) // 최신 날짜순 (내림차순) 정렬
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dates);

        } catch (IOException e) {
            System.err.println("날짜 폴더 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 2. [GET /api/video/files/{date}] 특정 날짜 폴더 내의 비디오 파일 목록을 반환합니다.
     */
    @GetMapping("/files/{date}")
    public ResponseEntity<List<String>> getVideosByDate(@PathVariable String date) {
        Path dateDir = Paths.get(videoBasePath, date);

        if (!Files.exists(dateDir) || !Files.isDirectory(dateDir)) {
            return ResponseEntity.ok(List.of()); // 폴더가 없으면 빈 리스트
        }

        try {
            // 폴더 내 파일 목록을 읽고, .mp4 등으로 끝나는 파일만 필터링하여 반환
            List<String> files = Files.list(dateDir)
                    .filter(p -> p.getFileName().toString().endsWith(".mp4") || p.getFileName().toString().endsWith(".avi"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .sorted() // 파일명으로 오름차순 정렬
                    .collect(Collectors.toList());

            return ResponseEntity.ok(files);

        } catch (IOException e) {
            System.err.println("비디오 파일 목록 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 3. [GET /api/video/stream/{date}/{filename}] 비디오 파일을 스트리밍합니다.
     * 브라우저의 <video> 태그가 Range 요청을 보내므로, FileSystemResource를 사용합니다.
     */
    @GetMapping("/stream/{date}/{filename}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String date, @PathVariable String filename) {
        try {
            Path filePath = Paths.get(videoBasePath, date, filename);
            File videoFile = filePath.toFile();

            if (!videoFile.exists() || !videoFile.isFile()) {
                return ResponseEntity.notFound().build(); // 파일이 없으면 404
            }

            // FileSystemResource를 사용하여 파일 시스템의 파일을 리소스로 감쌉니다.
            Resource resource = new FileSystemResource(videoFile);

            // Range 요청 처리를 위해 HttpHeaders에 Content-Type과 Content-Length를 설정합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4"); // 비디오 타입 지정
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoFile.length()));

            // Spring은 Resource를 반환할 때 자동으로 Range 헤더를 처리하여 스트리밍을 지원합니다.
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            System.err.println("비디오 스트리밍 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
