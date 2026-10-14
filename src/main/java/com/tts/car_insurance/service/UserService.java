package com.tts.car_insurance.service;

import com.tts.car_insurance.dto.request.UserRequest;
import com.tts.car_insurance.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse save(UserRequest userRequest);

    UserResponse findById(String id);

    List<UserResponse> findAll();

    UserResponse update(String id, UserRequest request);

    void delete(String id);
}
