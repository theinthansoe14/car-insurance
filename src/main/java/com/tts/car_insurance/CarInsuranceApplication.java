package com.tts.car_insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
public class CarInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInsuranceApplication.class, args);
	}

}
