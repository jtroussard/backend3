package com.dl4m.backend3.service;

import com.dl4m.backend3.entity.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User saveUser(User user);
}
