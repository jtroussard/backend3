package com.dl4m.backend3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    // TODO: Consider externalizing the BCrypt strength parameter for tuning in different environments.
    //       BCryptPasswordEncoder(int strength) allows adjusting the computational cost (default = 10).


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

