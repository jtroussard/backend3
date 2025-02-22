package com.dl4m.backend3.service;

import com.dl4m.backend3.entity.Role;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
