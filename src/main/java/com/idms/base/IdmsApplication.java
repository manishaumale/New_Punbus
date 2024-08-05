package com.idms.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IdmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdmsApplication.class, args);
	}

}
