package org.example.userservice.repository;

import org.example.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Object findByName(String name);
}
