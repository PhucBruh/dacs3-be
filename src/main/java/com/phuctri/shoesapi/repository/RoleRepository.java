package com.phuctri.shoesapi.repository;

import com.phuctri.shoesapi.entities.Role;
import com.phuctri.shoesapi.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
