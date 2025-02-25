package com.dl4m.backend3.repo;

import com.dl4m.backend3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// TODO consider removing the find by name getter as using codes will be more secure.

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByCode(int code);
}
