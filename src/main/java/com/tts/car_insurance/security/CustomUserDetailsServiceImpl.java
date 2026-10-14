package com.tts.car_insurance.security;

import com.tts.car_insurance.enums.ROLE;
import com.tts.car_insurance.exception.impl.UserNotFoundException;
import com.tts.car_insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.tts.car_insurance.model.User user = userRepository.findByEmail(email).orElseThrow(
                ()->new UserNotFoundException("User not found with the email " + email)
        );

        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE" + ROLE.resolve(user.getRole())))
        );

    }
}