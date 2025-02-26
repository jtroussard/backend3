package com.dl4m.backend3.controller;

import com.dl4m.backend3.dto.request.RegistrationRequest;
import com.dl4m.backend3.dto.response.RegistrationResponse;
import com.dl4m.backend3.entity.User;
import com.dl4m.backend3.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registerRequest) {
        log.debug("[CONTROLLER] register endpoint called");
        log.debug("TUNA Received password: {}", registerRequest.getPassword()); // Log received password

        log.info("BEFORE");
        User user = new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), Collections.emptySet());
        log.info("AFTER");
        User savedUser = userService.registerUser(user);

        RegistrationResponse response = new RegistrationResponse(savedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
