package com.example.takehometest.repository;


import com.example.takehometest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByRoleNameIgnoreCaseAndIsDeletedIsFalse(String roleName);

    Optional<Role> findByIdAndIsDeletedIsFalse(Long roleId);

    Optional<Role> findFirstByRoleName(String roleName);
}