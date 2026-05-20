package com.example.takehometest.validation;

import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.dto.request.EditUserRequest;
import com.example.takehometest.dto.request.LoginRequest;
import com.example.takehometest.dto.request.RegisterRequest;
import com.example.takehometest.dto.request.ResetPasswordRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserValidation {

    public void userLoginValidation(List<BaseErrorMessage> errorMessages, LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "Username cannot be empty"));
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("Password", "Password cannot be empty"));
        }
    }

    public void userRegisterValidation(List<BaseErrorMessage> errorMessages, RegisterRequest request) {
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("full_name", "Full name cannot be empty"));
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "Username cannot be empty"));
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("Password", "Password cannot be empty"));
        }
        if (request.getRoleId() == null) {
            errorMessages.add(new BaseErrorMessage("role_id", "Role id cannot be empty"));
        }
    }

    public void resetPasswordValidation(List<BaseErrorMessage> errorMessages, ResetPasswordRequest request) {
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("new_password", "New password cannot be empty"));
        }
        if (request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("confirm_password", "Confirm password cannot be empty"));
        }
    }

    public void editUserValidation (List<BaseErrorMessage> errorMessages, EditUserRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "Username cannot be empty"));
        }

        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            errorMessages.add(new BaseErrorMessage("full_name", "Full name cannot be empty"));
        }
        if (request.getRoleId() == null) {
            errorMessages.add(new BaseErrorMessage("role_id", "Role id cannot be empty"));
        }
    }

}
