package com.dl4m.backend3.security;

import com.dl4m.backend3.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.debug("[{}] Intercepted request: {} {}", this.getClass().getSimpleName(), request.getMethod(), request.getRequestURI());

        String token = extractJwtFromCookies(request);

        if (token != null) {
            log.debug("[{}] Token found in cookie", this.getClass().getSimpleName());

            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.extractUsername(token);
                log.debug("[{}] Extracted username: {}", this.getClass().getSimpleName(), username);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(username);

                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("[{}] Security context set for user: {}", this.getClass().getSimpleName(), username);
                } else {
                    log.debug("[{}] Security context already contains authentication", this.getClass().getSimpleName());
                }
            } else {
                log.warn("[{}] Invalid JWT token received", this.getClass().getSimpleName());
            }
        } else {
            log.debug("[{}] No JWT token found in cookies", this.getClass().getSimpleName());
        }

        chain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
