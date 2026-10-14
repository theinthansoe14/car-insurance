package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.dto.request.LoginRequest;
import com.tts.car_insurance.dto.response.LoginResponse;
import com.tts.car_insurance.exception.impl.UserNotFoundException;
import com.tts.car_insurance.model.User;
import com.tts.car_insurance.repository.UserRepository;
import com.tts.car_insurance.service.AuthenticationService;
import com.tts.car_insurance.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = this.authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException e) {
            logger.warn("SECURITY [WARN]: Login FAILED for user: {}. Reason: {}", request.email(), e.getMessage());
            throw new BadCredentialsException("user Name or Password is incorrect");
        }

        logger.info("SECURITY [INFO]: Login SUCCESS for user: {}", authentication.getName());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()->new UserNotFoundException("User not found") );


        String token = this.jwtUtil.generateToken(user) ;




        return new  LoginResponse(
                user.getEmail(),
                token
        );
    }
}
