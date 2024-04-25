package com.capstone.Carvedream;

import com.capstone.Carvedream.global.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
public class CarveDreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarveDreamApplication.class, args);
	}

}
