package com.tts.car_insurance.controller;


import com.tts.car_insurance.dto.request.LoginRequest;
import com.tts.car_insurance.dto.response.LoginResponse;
import com.tts.car_insurance.service.AuthenticationService;
import com.tts.car_insurance.service.impl.AuthenticationServiceImpl;
import com.tts.car_insurance.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication",description = "Login Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserServiceImpl userService;

    @Operation(summary = "login user",description = "login with email and password and return token")
    @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping ("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(service.login(request));


    }



}

