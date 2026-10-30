package com.tts.car_insurance.repository;

import com.tts.car_insurance.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomUserRepository {

    User updateUserDetails(String id, String name, String email, String password, LocalDate dateOfBirth, LocalDateTime updatedAt);
}
