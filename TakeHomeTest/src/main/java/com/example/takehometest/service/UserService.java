package com.example.takehometest.service;

import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import com.example.takehometest.dto.request.EditUserRequest;
import com.example.takehometest.dto.request.LoginRequest;
import com.example.takehometest.dto.request.RegisterRequest;
import com.example.takehometest.dto.request.ResetPasswordRequest;
import com.example.takehometest.dto.response.LoginResponse;
import com.example.takehometest.dto.response.RoleListResponse;
import com.example.takehometest.dto.response.UserDetailResponse;
import com.example.takehometest.dto.response.UserListResponse;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.User;
import com.example.takehometest.repository.FeatureRepository;
import com.example.takehometest.repository.RoleRepository;
import com.example.takehometest.repository.UserRepository;
import com.example.takehometest.specification.UserSpecification;
import com.example.takehometest.validation.UserValidation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FeatureRepository featureRepository;
    private final RoleRepository roleRepository;
    private final UserSpecification userSpecification;

    public LoginResponse login(LoginRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate user login request
        userValidation.userLoginValidation(errorMessages, request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(request.getUsername());

        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("login", "Username or password is incorrect"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();

        boolean isAdmin = user.getRole().getIsAdmin();

        List<String> permissions = new ArrayList<>();



        if (!isAdmin) {
            permissions = featureRepository.getFeatureIdByRoleId(user.getRole().getId());
            for (String feature : permissions) {
                System.out.println("feature : " + feature);
            }
        }

        return new LoginResponse(isAdmin,permissions);
    }

    public void register(RegisterRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate user register request
        userValidation.userRegisterValidation(errorMessages, request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(request.getUsername());

        if (userOpt.isPresent()) {
            errorMessages.add(new BaseErrorMessage("username", "Username already exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        Optional<Role> roleOptional = roleRepository.findByIdAndIsDeletedIsFalse(request.getRoleId());
        if (roleOptional.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("role", "Role doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        user.setRole(roleOptional.get());
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate user register request
        userValidation.resetPasswordValidation(errorMessages, request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            errorMessages.add(new BaseErrorMessage("password", "New password not equals with the confirm password"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "User doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            errorMessages.add(new BaseErrorMessage("old_password", "Old password is incorrect"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void editUser (EditUserRequest request, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        // Validate User Edit
        userValidation.editUserValidation(errorMessages,request);

        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(request.getUsername());
        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "User doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();
        user.setFullName(request.getFullName());

        Optional<Role> roleOptional = roleRepository.findByIdAndIsDeletedIsFalse(request.getRoleId());
        if (roleOptional.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("role", "Role doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        user.setRole(roleOptional.get());
        userRepository.save(user);
    }

    public void deleteUser (String username, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "User doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();

        if (user.getRole().getIsAdmin()) {
            errorMessages.add(new BaseErrorMessage("superadmin", "User cannot be deleted, because its superadmin"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    public void changeStatusUser (String username, Boolean statusUser, BaseResponse<String> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("username", "User doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();

        if (user.getRole().getIsAdmin() && statusUser.equals(false)) {
            errorMessages.add(new BaseErrorMessage("superadmin", "User cannot be inactive, because its superadmin"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }
        user.setIsActive(statusUser);
        userRepository.save(user);
    }

    public Page<UserListResponse> showListUser (
            String search, Integer page, Integer limit, String orderBy, Boolean asc
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> userPage = userRepository.findAll(
                userSpecification.sortBy(orderBy,asc)
                        .and(userSpecification.findBySearch(search))

                , pageable);
        List<UserListResponse> roleFeatureResponseList = mappingUserList(userPage.getContent());

        return new PageImpl<>(roleFeatureResponseList, pageable, userPage.getTotalElements());
    }

    private List<UserListResponse> mappingUserList (List<User> userPage) {
        List<UserListResponse> userResponseList =  new ArrayList<>();

        for (User user : userPage) {
            userResponseList.add(new UserListResponse(user.getId(),user.getUsername(),user.getFullName(), user.getRole().getRoleName()));
        }

        return userResponseList;
    }

    public UserDetailResponse showDetailUser (String username, BaseResponse<UserDetailResponse> response) {
        List<BaseErrorMessage> errorMessages = new ArrayList<>();

        Optional<User> userOpt =  userRepository.findByUsernameAndIsDeletedIsFalse(username);

        if (userOpt.isEmpty()) {
            errorMessages.add(new BaseErrorMessage("id", "User doesn't exists"));
            response.setErrorMessages(errorMessages);
            throw new ValidationException();
        }

        User user = userOpt.get();
        Role roleUser = user.getRole();
        return new UserDetailResponse(user.getId(),user.getUsername(),user.getFullName(),roleUser.getId(),roleUser.getRoleName(), user.getIsActive());
    }
}
