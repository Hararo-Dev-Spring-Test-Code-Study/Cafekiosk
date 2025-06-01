package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
//이 클래스가 Spring Boot 어플리케이션의 시작점이라는 선언
//이 클래스의 패키지부터 하위 구조를 스캔해서 필요한 모든 것들을 자동으로 구성
// @SpringBootApplication은 아래 세가지 annotation이 합쳐진것
// @configuration(설정클래스라는 것을 알림)
// @EnableAutoConfiguration(Spring Boot자동설정을 킴)
// @ComponentScan(이 클래스가 속한 패키지부터 @Component들을 찾아서 Bean으로 등록)
// Bean이란 Spring이 만들어서 관리하는 객체(==인스턴스)
@SpringBootApplication
public class CafekioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafekioskApplication.class, args);
    }

}

// 같음
// package sample.cafekiosk.spring;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class CafekioskApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(CafekioskApplication.class, args);
//     }

// }
