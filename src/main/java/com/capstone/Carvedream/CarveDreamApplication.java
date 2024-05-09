package com.capstone.Carvedream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CarveDreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarveDreamApplication.class, args);
	}

}
