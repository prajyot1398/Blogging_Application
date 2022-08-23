package com.bloggingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloggingapi.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    
    Role findByRoleName(String roleName);

    boolean existsRoleByRoleName(String roleName);
}
