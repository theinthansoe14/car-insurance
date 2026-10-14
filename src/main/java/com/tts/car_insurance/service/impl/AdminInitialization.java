package com.tts.car_insurance.service.impl;

import com.tts.car_insurance.model.User;
import com.tts.car_insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        boolean adminExist = userRepository.existsByRole("admin");

        if(!adminExist){

            User admin = User.builder()
                    .email("admin@gmail.com")
                    .name("Admin")
                    .role("admin")
                    .password(passwordEncoder.encode("admin"))
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);

            log.info(" [DEFAULT ADMIN CREATED SUCCESSFULLY]");

        }else
            log.info("Admin account already exist");



    }
}
