// 웹관련 설정 파일

package com.example.capston_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // @Configuration으로 선언된 클래스는 애플리케이션이 실행될 떄 자동으로 로딩 전체에 적용
public class WebConfig {
    // CORS 설정 (다른 포트끼리 통신할 수 있게 설정 그냥하면 보안정책 떄문에 안됨) 부분
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8080") // Vue 개발 서버 주소
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    // CORS 설정 여기까지
}
