package com.example.elms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElmsApplication.class, args);
	}
}
