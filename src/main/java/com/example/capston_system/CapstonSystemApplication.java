//스프링 프로젝트의 시작점 다른 모든 파일들 여기로 부터 시작
package com.example.capston_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 전체 프로젝트에서 스케줄 기능을 사용하겠다 명시
public class CapstonSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstonSystemApplication.class, args);
	}

}

