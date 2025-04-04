package com.dl4m.backend3.controller;

import com.dl4m.backend3.dto.request.LoginRequest;
import com.dl4m.backend3.security.CustomUserDetails;
import com.dl4m.backend3.security.JwtUtils;
import com.dl4m.backend3.service.CustomUserDetailsService;
import com.dl4m.backend3.utils.CloudUtils;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // TODO: Consider extracting cookie creation to a utility for reuse and testability.
    //       Also evaluate SameSite="Strict" or "Lax" depending on CSRF strategy.

    CustomUserDetailsService userDetailsService;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    CloudUtils cloudUtils;

    @Autowired
    public AuthController(
            CustomUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            CloudUtils cloudUtils
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cloudUtils =  cloudUtils;
    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        log.debug("[{}] login endpoint called", this.getClass().getSimpleName());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new IllegalStateException("Authentication principal is not an instance of CustomUserDetails");
        }
        String jwt = jwtUtils.generateToken(userDetails);


        Cookie jwtCookie = new Cookie("jwt", jwt);
        boolean cookieSecureSetting = cloudUtils.isRunningInGCP();
        log.debug("[{}] Running in GCP: {}", this.getClass().getSimpleName(), cookieSecureSetting);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setAttribute("SameSite", "None");
        jwtCookie.setSecure(cookieSecureSetting);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtUtils.getExpiration() / 1000));
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Login successful");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        log.debug("[{}] me endpoint called", this.getClass().getSimpleName());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("username", userDetails.getUsername());
        response.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping("/test/protected")
    public ResponseEntity<String> protectedTest() {
        log.debug("[{}] protectedTest endpoint called", this.getClass().getSimpleName());
        return ResponseEntity.ok("You're good!");
    }

    @PermitAll
    @GetMapping("/test/public")
    public ResponseEntity<String> publicTest() {
        log.debug("[{}] publicTest endpoint called", this.getClass().getSimpleName());
        return ResponseEntity.ok("This is a public endpoint.");
    }

}
