package com.example.postgretest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PostgretestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgretestApplication.class, args);
	}

}
