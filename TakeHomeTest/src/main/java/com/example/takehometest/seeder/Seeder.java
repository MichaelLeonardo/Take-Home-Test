package com.example.takehometest.seeder;

import com.example.takehometest.model.Feature;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.RoleFeature;
import com.example.takehometest.model.User;
import com.example.takehometest.repository.FeatureRepository;
import com.example.takehometest.repository.RoleFeatureRepository;
import com.example.takehometest.repository.RoleRepository;
import com.example.takehometest.repository.UserRepository;
import com.example.takehometest.utils.PropertyHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Seeder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PropertyHelper propertyHelper;
    private final FeatureRepository featureRepository;
    private final RoleFeatureRepository roleFeatureRepository;

    @PostConstruct
    public void seed() {
        seedRole();
        seedUser();
        seedFeatureOnly();
        seedRoleFeature();
    }

    private void seedRole() {
        if (roleRepository.count() == 0) {
            logger.info("Populating Role Data.");

            // Superadmin role
            Role superAdmin = new Role(null, "Super Admin", null, true, null);
            // Manager role
            Role manager = new Role(null, "Manager", "Second Level", false, null);
            // Employee role
            Role employee = new Role(null, "Employee", "First Level", false, null);

            roleRepository.save(superAdmin);
            roleRepository.save(manager);
            roleRepository.save(employee);

        }
    }

    private void seedUser() {
        if (userRepository.count() == 0) {
            logger.info("Populating User Data.");

            Role roleSuperAdmin = roleRepository.findFirstByRoleName("Super Admin").get();
            User userSuperAdmin = new User(null,"super admin", "superadmin",
                    BCrypt.hashpw(propertyHelper.getSuperadminPassword(), BCrypt.gensalt()),roleSuperAdmin, true, 12);

            Role roleManager = roleRepository.findFirstByRoleName("Manager").get();
            User manager = new User(null,"Manager", "manager",
                    BCrypt.hashpw(propertyHelper.getSuperadminPassword(), BCrypt.gensalt()),roleManager, true, 12);

            Role roleEmployee = roleRepository.findFirstByRoleName("Employee").get();
            User employee = new User(null,"Employee", "employee",
                    BCrypt.hashpw(propertyHelper.getSuperadminPassword(), BCrypt.gensalt()),roleEmployee, true, 12);

            userRepository.save(userSuperAdmin);
            userRepository.save(manager);
            userRepository.save(employee);
        }
    }

    private void seedFeatureOnly() {
        if (featureRepository.count() == 0) {
            logger.info("Populating Menu.");

            //seed menu
            Feature feature1 = new Feature(1L,"User","/user/**","user_management", "User Magagement");
            Feature feature2 = new Feature(2L,"Role","/role/**","user_management", "User Magagement");
            Feature feature3 = new Feature(3L,"Leave Request","/leave/request/**","leave_request", "Leave");
            Feature feature4 = new Feature(4L,"Leave Approval","/leave/approval/**","leave_approval", "Leave");

            featureRepository.save(feature1);
            featureRepository.save(feature2);
            featureRepository.save(feature3);
            featureRepository.save(feature4);
        }
    }

    private void seedRoleFeature() {
        if (roleFeatureRepository.count() == 0) {
            logger.info("Populating Role Feature.");

            // Seed feature for role manager
            Role roleManager = roleRepository.findFirstByRoleName("Manager").get();
            List<Long> listFeatureManager = new ArrayList<>();
            listFeatureManager.add(1L);
            listFeatureManager.add(2L);
            listFeatureManager.add(3L);
            listFeatureManager.add(4L);
            List<Feature> listFeature = featureRepository.findAllByIdIsInAndIsDeletedIsFalse(listFeatureManager);
            for (Feature feature : listFeature) {
                roleFeatureRepository.save(new RoleFeature(null, roleManager, feature));
            }

            // Seed feature for role employee
            Role roleEmployee = roleRepository.findFirstByRoleName("Employee").get();
            Optional<Feature> featureOpt = featureRepository.findByIdAndIsDeletedIsFalse(3L);
            roleFeatureRepository.save(new RoleFeature(null, roleEmployee, featureOpt.get()));

        }
    }
}
