package com.tts.car_insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class CarInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInsuranceApplication.class, args);
	}

}
