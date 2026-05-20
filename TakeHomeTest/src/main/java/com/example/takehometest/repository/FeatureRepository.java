package com.example.takehometest.repository;

import com.example.takehometest.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    @Query(nativeQuery = true, value =
            "SELECT f.menu_url "
                + " FROM t_role_feature as rf "
                + " JOIN t_feature as f ON f.id = rf.feature_id "
                + " WHERE rf.role_id = :roleId"
    )
    List<String> getFeatureIdByRoleId (@Param("roleId") Long roleId);

    List<Feature> findAllByIdIsInAndIsDeletedIsFalse (List<Long> listIdFeature);

    @Query(nativeQuery = true, value =
            "SELECT f.feature_name "
                    + " FROM t_role_feature as rf "
                    + " JOIN t_feature as f ON f.id = rf.feature_id "
                    + " WHERE rf.role_id = :roleId"
    )
    List<String> getFeatureByRoleId (@Param("roleId") Long roleId);

    Optional<Feature> findByIdAndIsDeletedIsFalse(Long id);
}
