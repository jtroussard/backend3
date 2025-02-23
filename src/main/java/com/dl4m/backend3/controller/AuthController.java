package com.dl4m.backend3.controller;

import com.dl4m.backend3.security.JwtUtils;
import com.dl4m.backend3.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Map;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        log.debug("[CONTROLLER] login endpoint called");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
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
// spoofed response for roles to test frontedn integration
//        return ResponseEntity.ok(Map.of(
//                "message", "Login successful",
//                "roles", Arrays.asList(2001, 1984, 5150, 1337)
//        ));
    }

    @GetMapping("/test/protected")
    public ResponseEntity<String> protectedTest() {
        log.debug("[CONTROLLER] protectedTest endpoint called");
        return ResponseEntity.ok("You're good!");
    }

    @GetMapping("/test/public")
    public ResponseEntity<String> publicTest() {
        log.debug("[CONTROLLER] publicTest endpoint called");
        return ResponseEntity.ok("This is a public endpoint.");
    }

}
