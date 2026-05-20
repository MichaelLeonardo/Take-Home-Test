package com.example.takehometest.service;

import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import com.example.takehometest.dto.request.RoleAddEditRequest;
import com.example.takehometest.dto.response.RoleFeatureResponse;
import com.example.takehometest.dto.response.RoleListResponse;
import com.example.takehometest.model.Feature;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.RoleFeature;
import com.example.takehometest.repository.FeatureRepository;
import com.example.takehometest.repository.RoleFeatureRepository;
import com.example.takehometest.repository.RoleRepository;
import com.example.takehometest.specification.RoleSpecification;
import com.example.takehometest.validation.RoleValidation;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.example.takehometest.base.BasePageInterface;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleValidation roleValidation;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleFeatureRepository roleFeatureRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private RoleSpecification roleSpecification;

    public void addRoleFeature (RoleAddEditRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate role request
        roleValidation.addEditRoleValidation(errorMessages, request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        //Check duplicate role name
        if (roleRepository.findByRoleNameIgnoreCaseAndIsDeletedIsFalse(request.getRoleName()).isPresent()) {
            errorMessages.add(new BaseErrorMessage("role_name", "Role name already exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setIsAdmin(request.getIsAdmin());
        role.setFeatureList(null);
        roleRepository.save(role);

        roleFeatureRepository.deleteByRoleId(role.getId());
        if (request.getFeatureList() != null && !request.getFeatureList().isEmpty()) {
            List<Feature> listFeature = featureRepository.findAllByIdIsInAndIsDeletedIsFalse(request.getFeatureList());
            for (Feature feature : listFeature) {
                roleFeatureRepository.save(new RoleFeature(null, role, feature));
            }
        }

    }

    public void editRoleFeature (Long id, RoleAddEditRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate role request
        roleValidation.addEditRoleValidation(errorMessages, request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Optional<Role> roleOpt = roleRepository.findByIdAndIsDeletedIsFalse(id);
        if (roleOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("role", "Role is doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        //Check duplicate role name
        if (roleRepository.findByRoleNameIgnoreCaseAndIsDeletedIsFalse(request.getRoleName()).isPresent()) {
            errorMessages.add(new BaseErrorMessage("role_name", "Role name already exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Role role = roleOpt.get();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setIsAdmin(request.getIsAdmin());
        role.setFeatureList(null);
        roleRepository.save(role);

        roleFeatureRepository.deleteByRoleId(role.getId());
        if (request.getFeatureList() != null && !request.getFeatureList().isEmpty()) {
            List<Feature> listFeature = featureRepository.findAllByIdIsInAndIsDeletedIsFalse(request.getFeatureList());
            for (Feature feature : listFeature) {
                roleFeatureRepository.save(new RoleFeature(null, role, feature));
            }
        }

    }

    public Page<RoleListResponse> getAllRoleFeatureResponse (
            String search, Integer page, Integer limit, String orderBy, Boolean asc
    ) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<Role> rolePage = roleRepository.findAll(
                        roleSpecification.sortBy(orderBy,asc)
                        .and(roleSpecification.findBySearch(search))
                , pageable);
        List<RoleListResponse> roleFeatureResponseList = mappingRoleList(rolePage.getContent());

        return new PageImpl<>(roleFeatureResponseList, pageable, rolePage.getTotalElements());
    }

    private List<RoleListResponse> mappingRoleList (List<Role> rolePage) {
        List<RoleListResponse> roleFeatureResponseList =  new ArrayList<>();

        for (Role role : rolePage) {
            roleFeatureResponseList.add(new RoleListResponse(role.getId(),role.getRoleName()));
        }

        return  roleFeatureResponseList;
    }


}
