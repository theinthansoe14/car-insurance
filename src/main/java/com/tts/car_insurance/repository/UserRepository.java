package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String>,CustomUserRepository{

    Optional<User> findByEmail(String email);

    long deleteUserById(String id);

    boolean existsByRole(String admin);

    boolean existsByEmail(String email);
}
