package com.example.takehometest.validation;

import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.dto.request.RoleAddEditRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleValidation {

    public void addEditRoleValidation(List<BaseErrorMessage> errorMessages, RoleAddEditRequest request) {
        if (request.getRoleName() == null || request.getRoleName().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("role_name", "Role name cannot be empty"));
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("description", "Description cannot be empty"));
        }
        if (request.getIsAdmin() == null) {
            errorMessages.add(new BaseErrorMessage("is_admin", "Is Admin cannot be empty"));
        }
    }
}
