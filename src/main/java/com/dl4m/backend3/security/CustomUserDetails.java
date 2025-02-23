package com.dl4m.backend3.security;

import com.dl4m.backend3.entity.Role;
import com.dl4m.backend3.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify if implementing expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify if implementing account lock logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify if implementing password expiration
    }

    @Override
    public boolean isEnabled() {
        return true; // Modify if implementing account enabling logic
    }
}
