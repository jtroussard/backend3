package com.dl4m.backend3.service;

import com.dl4m.backend3.entity.Role;
import com.dl4m.backend3.repo.RoleRepo;
import com.dl4m.backend3.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepo.findByName(name);
    }
}
