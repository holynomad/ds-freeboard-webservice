package com.ds.freeboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableJpaAuditing <-- @WebMvcTest 연동위해 주석(삭제) + JpaConfig 별도 분리(추가) @ 2020.11.26.
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
