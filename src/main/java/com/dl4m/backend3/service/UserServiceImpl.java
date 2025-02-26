package com.dl4m.backend3.service;

import com.dl4m.backend3.entity.Role;
import com.dl4m.backend3.entity.User;
import com.dl4m.backend3.exception.RegistrationException;
import com.dl4m.backend3.exception.UsernameAlreadyExistsException;
import com.dl4m.backend3.repo.RoleRepo;
import com.dl4m.backend3.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' is already taken");
        }

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RegistrationException("Default role not found"));
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }
}
