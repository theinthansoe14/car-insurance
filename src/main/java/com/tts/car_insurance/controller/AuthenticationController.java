package com.tts.car_insurance.controller;


import com.tts.car_insurance.dto.request.LoginRequest;
import com.tts.car_insurance.dto.response.LoginResponse;
import com.tts.car_insurance.service.impl.AuthenticationServiceImpl;
import com.tts.car_insurance.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl service;
    private final UserServiceImpl userService;

    @PostMapping ("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(service.login(request));


    }



}

