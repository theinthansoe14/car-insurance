package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<Vehicle,String> {
}
