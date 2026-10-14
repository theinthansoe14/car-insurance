package com.tts.car_insurance.service;

import com.tts.car_insurance.dto.request.LoginRequest;
import com.tts.car_insurance.dto.response.LoginResponse;

public interface AuthenticationService {

LoginResponse login(LoginRequest request);

}
