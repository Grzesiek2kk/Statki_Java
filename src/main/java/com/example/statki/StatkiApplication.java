package com.example.statki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.statki,model")
@EnableJpaRepositories("com.example.statki.repo")
public class StatkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatkiApplication.class, args);
	}

}
