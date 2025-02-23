package com.dl4m.backend3.controller;

import com.dl4m.backend3.entity.User;
import com.dl4m.backend3.security.CustomUserDetails;
import com.dl4m.backend3.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDetails> login(@RequestParam String username,@RequestParam String password) {
        log.debug("[CONTROLLER] login endpoint called");
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(Exception err) {
            log.error("Something went horribly wrong! {}", err.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
