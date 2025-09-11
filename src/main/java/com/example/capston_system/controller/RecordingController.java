//package com.example.capston_system.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//
//public class RecordingController {
//    private final String FLASK_SERVER = "http://192.168.76.98:5000";
//
//    /*@GetMapping("/dates")
//    public ResponseEntity<List<String>> getAvailableDates() {
//        File baseDir = new File("/home/pc/toy_cat/venv/Video/recordings");
//        String[] dirs = baseDir.list((file, name) -> new File(file, name).isDirectory());
//        return ResponseEntity.ok(Arrays.asList(dirs));
//    }
//    */
//
//    @GetMapping("/dates")
//    public ResponseEntity<List<String>> getAvailableDates() {
//        File baseDir = new File("/home/pc/toy_cat/venv/Video/recordings");
//
//        // 1. 경로가 존재하고 디렉토리인지 확인
//        if (!baseDir.exists()) {
//            System.out.println("Exists: " + baseDir.exists());
//            System.out.println("Is Directory: " + baseDir.isDirectory());
//            System.out.println("Can Read: " + baseDir.canRead());
//            System.out.println("Can Write: " + baseDir.canWrite());
//            System.out.println("Can Execute: " + baseDir.canExecute());
//            System.out.println("디렉토리 존재하지 않음!");
//            return ResponseEntity.ok(List.of()); // 빈 리스트 반환
//        }
//
//        if (!baseDir.isDirectory()) {
//            System.out.println("    디렉토리가 아님!");
//            return ResponseEntity.ok(List.of()); // 빈 리스트 반환
//        }
//
//        // 2. 폴더 읽기
//        String[] dirs = baseDir.list((file, name) -> new File(file, name).isDirectory());
//
//        // 3. null 처리
//        if (dirs == null) {
//            System.out.println("/O 오류, 읽기 권한 없음, 비정상적 접근!");
//            return ResponseEntity.ok(List.of()); // 빈 리스트로 처리
//        }
//
//        return ResponseEntity.ok(Arrays.asList(dirs));
//    }
//
//    @GetMapping("/videos/{date}")
//    public ResponseEntity<String> getVideos(@PathVariable String date) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = FLASK_SERVER + "/videos/" + date;
//        return restTemplate.getForEntity(url, String.class);
//    }
//
//    @GetMapping("/video-stream/{date}/{filename}")
//    public void streamVideo(@PathVariable String date, @PathVariable String filename, HttpServletResponse response) throws IOException {
//        String videoUrl = FLASK_SERVER + "/video/" + date + "/" + filename;
//        URL url = new URL(videoUrl);
//        try (InputStream in = url.openStream()) {
//            response.setContentType("video/mp4");
//            IOUtils.copy(in, response.getOutputStream());
//        }
//    }
//}
//package com.example.capston_system.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//public class RecordingController {
//    @GetMapping("/api/video-stream/{date}/{filename}")
//    public void proxyVideoStream(@PathVariable String date,
//                                 @PathVariable String filename,
//                                 HttpServletResponse response) throws IOException {
//
//        String flaskUrl = String.format("http://192.168.76.98:5000/video/recordings/%s/%s", date, filename);
//        URL url = new URL(flaskUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//
//        response.setContentType("video/mp4");
//        response.setStatus(conn.getResponseCode());
//
//        try (InputStream is = conn.getInputStream();
//             OutputStream os = response.getOutputStream()) {
//
//            byte[] buffer = new byte[8192];
//            int bytesRead;
//
//            while ((bytesRead = is.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//        }
//    }
//}

package com.example.capston_system.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController // REST API용 컨트롤러임을 명시. 각 메서드는 JSON 또는 Stream 등으로 데이터를 반환
// @RequestMapping 스프링 애플리케이션에서 클라이언트의 HTTP 요청을 특정 자바 코드(컨트롤러 메서드)와 연결해주는 핵심적인 어노테이션
@RequestMapping("/api") // 이 클래스의 모든 메서드는 "/api"로 시작하는 경로에서 호출 가능.
public class RecordingController {

    // 연결할 Flask 서버 주소. 이 서버에서 비디오 관련 데이터가 제공됨.
    private final String FLASK_SERVER = "http://10.107.35.98:5000";
    //private static final String VIDEO_BASE_PATH = "/home/pc/toy_cat/venv/Video/recordings"; // 비디오 저장위치

    // 🎥 [GET] 날짜 목록을 가져오는 메서드
    @GetMapping("/dates")
    public List<String> getDates() throws IOException {
        // Flask 서버의 /videos 엔드포인트에 GET 요청
        URL url = new URL(FLASK_SERVER + "/videos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 결과를 저장할 리스트
        List<String> dates = new ArrayList<>();

        // 응답 스트림을 읽고 JSON 형식의 문자열로 변환
        try (InputStream is = conn.getInputStream()) {
            String json = new String(is.readAllBytes());
            // JSON 배열 파싱: ["2024-06-10", "2024-06-11", ...]

            // JSON 배열 문자열에서 괄호([])와 따옴표("") 제거
            json = json.replaceAll("[\\[\\]\"]", "");

            // 문자열이 비어있지 않으면 쉼표(,) 기준으로 분리하여 날짜 리스트 생성
            if (!json.isEmpty()) {
                String[] items = json.split(",");
                for (String item : items) {
                    dates.add(item.trim());
                }
            }
        }

        return dates; // 날짜 리스트 반환
    }



    @GetMapping("/videos/{date}")
    public List<String> getVideosByDate(@PathVariable String date) throws IOException {
        URL url = new URL(FLASK_SERVER + "/videos/" + date);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        List<String> videos = new ArrayList<>();

        try (InputStream is = conn.getInputStream()) {
            String json = new String(is.readAllBytes());
            json = json.replaceAll("[\\[\\]\"]", "");
            if (!json.isEmpty()) {
                String[] items = json.split(",");
                for (String item : items) {
                    videos.add(item.trim());
                }
            }
        }

        return videos;
    }

    @GetMapping("/video-stream/{date}/{filename}")
    public void proxyVideoStream(@PathVariable String date,
                                 @PathVariable String filename,
                                 HttpServletResponse response,
                                 @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        System.out.println("=== 비디오 스트리밍 요청 시작 ===");
        System.out.println("📅 Date: " + date);
        System.out.println("📁 Filename: " + filename);
        System.out.println("🔄 Range Header: " + rangeHeader);

        String flaskUrl = String.format(FLASK_SERVER + "/video/recordings/%s/%s", date, filename);
        System.out.println("🌐 Flask URL: " + flaskUrl);

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            URL url = new URL(flaskUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 타임아웃 설정 추가
            conn.setConnectTimeout(10000); // 10초
            conn.setReadTimeout(30000);    // 30초

            if (rangeHeader != null) {
                conn.setRequestProperty("Range", rangeHeader);
                System.out.println("🔄 Range 헤더 설정됨: " + rangeHeader);
            }

            conn.connect();
            int responseCode = conn.getResponseCode();
            System.out.println("📡 Flask 응답 코드: " + responseCode);

            // 에러 응답 처리
            if (responseCode >= 400) {
                System.err.println("❌ Flask 서버 에러: " + responseCode);

                // 에러 스트림 읽기
                try (InputStream errorStream = conn.getErrorStream()) {
                    if (errorStream != null) {
                        String errorMessage = new String(errorStream.readAllBytes());
                        System.err.println("❌ 에러 메시지: " + errorMessage);
                    }
                }

                response.setStatus(responseCode);
                return;
            }

            // 헤더 복사
            String[] headerKeys = {"Content-Range", "Accept-Ranges", "Content-Length", "Content-Type"};
            for (String header : headerKeys) {
                String value = conn.getHeaderField(header);
                if (value != null) {
                    response.setHeader(header, value);
                    System.out.println("✅ 헤더 복사: " + header + " = " + value);
                }
            }

            response.setStatus(responseCode);

            // 스트림 처리 - 더 안전하게
            is = conn.getInputStream();
            os = response.getOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;
            long startTime = System.currentTimeMillis();

            System.out.println("📤 데이터 전송 시작...");

            while ((bytesRead = is.read(buffer)) != -1) {
                try {
                    os.write(buffer, 0, bytesRead);
                    os.flush(); // 즉시 전송
                    totalBytes += bytesRead;

                    // 진행상황 로그 (1MB마다)
                    if (totalBytes % (1024 * 1024) == 0) {
                        System.out.println("📊 전송 중: " + (totalBytes / 1024 / 1024) + "MB");
                    }

                } catch (IOException e) {
                    System.err.println("❌ 클라이언트 연결 끊어짐: " + e.getMessage());
                    break;
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("📊 전송 완료: " + totalBytes + " bytes (" + (endTime - startTime) + "ms)");

        } catch (Exception e) {
            System.err.println("❌ 스트림 처리 중 전체 오류: " + e.getMessage());
            e.printStackTrace();

            // 클라이언트에 에러 응답
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } finally {
            // 리소스 정리
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                System.err.println("❌ OutputStream 닫기 실패: " + e.getMessage());
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.err.println("❌ InputStream 닫기 실패: " + e.getMessage());
            }

            if (conn != null) {
                conn.disconnect();
            }

            System.out.println("=== 비디오 스트리밍 완료 ===");
        }
    }

//    @GetMapping("/video-stream/{date}/{filename}")
//    public void proxyVideoStream(@PathVariable String date,
//                                 @PathVariable String filename,
//                                 HttpServletResponse response,
//                                 @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
//
//        String flaskUrl = String.format(FLASK_SERVER + "/video/recordings/%s/%s", date, filename);
//        URL url = new URL(flaskUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//
//        if (rangeHeader != null) {
//            conn.setRequestProperty("Range", rangeHeader);
//        }
//
//        conn.connect();
//
//        // 복사할 헤더들
//        String[] headerKeys = {"Content-Range", "Accept-Ranges", "Content-Length", "Content-Type"};
//        for (String header : headerKeys) {
//            String value = conn.getHeaderField(header);
//            if (value != null) {
//                response.setHeader(header, value);
//            }
//        }
//
//        response.setStatus(conn.getResponseCode());
//
//        try (InputStream is = conn.getInputStream();
//             OutputStream os = response.getOutputStream()) {
//
//            byte[] buffer = new byte[8192];
//            int bytesRead;
//            while ((bytesRead = is.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//        }
//    }
}

