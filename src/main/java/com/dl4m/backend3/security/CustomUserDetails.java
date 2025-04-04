package com.dl4m.backend3.security;

import com.dl4m.backend3.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User domainUser;

    public CustomUserDetails(User domainUser) {
        this.domainUser = domainUser;
    }

    @Override
    public String getUsername() {
        return domainUser.getUsername();
    }

    @Override
    public String getPassword() {
        return domainUser.getPassword();
    }

    // TODO maybe think about using the role name for the backend and the role code for the frontend.
    // Role name to follow spring boot conventions, and code for frontend as an added layer of obfuscation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return domainUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
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
