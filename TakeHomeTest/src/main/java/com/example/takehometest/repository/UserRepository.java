package com.example.takehometest.repository;

import com.example.takehometest.model.Role;
import com.example.takehometest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
    Optional<User> findByIdAndIsDeletedIsFalse(Long id);
    List<User> findAllByIsDeletedIsFalse();

}
