package com.example.takehometest.repository;

import com.example.takehometest.model.RoleFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleFeatureRepository extends JpaRepository<RoleFeature, Long> {

    List<RoleFeature> findAllByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}
