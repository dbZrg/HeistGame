package com.db.heistgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HeistGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeistGameApplication.class, args);
	}

}
