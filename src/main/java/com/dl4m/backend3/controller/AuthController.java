package com.dl4m.backend3.controller;

import com.dl4m.backend3.dto.request.LoginRequest;
import com.dl4m.backend3.security.JwtUtils;
import com.dl4m.backend3.service.CustomUserDetailsService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    CustomUserDetailsService userDetailsService;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    @Autowired
    public AuthController(
            CustomUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        log.debug("[CONTROLLER] login endpoint called");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication.getPrincipal());

        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Set to true for HTTPS in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtUtils.getExpiration() / 1000));
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Login successful");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping("/test/protected")
    public ResponseEntity<String> protectedTest() {
        log.debug("[CONTROLLER] protectedTest endpoint called");
        return ResponseEntity.ok("You're good!");
    }

    @PermitAll
    @GetMapping("/test/public")
    public ResponseEntity<String> publicTest() {
        log.debug("[CONTROLLER] publicTest endpoint called");
        return ResponseEntity.ok("This is a public endpoint.");
    }

}
