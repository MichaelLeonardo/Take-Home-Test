package com.example.takehometest.controller;

import com.example.takehometest._enum.ResponseCodeEnum;
import com.example.takehometest.auth.JwtUtil;
import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import com.example.takehometest.dto.request.EditUserRequest;
import com.example.takehometest.dto.request.LoginRequest;
import com.example.takehometest.dto.request.RegisterRequest;
import com.example.takehometest.dto.request.ResetPasswordRequest;
import com.example.takehometest.dto.response.LoginResponse;
import com.example.takehometest.dto.response.UserDetailResponse;
import com.example.takehometest.dto.response.UserListResponse;
import com.example.takehometest.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody LoginRequest request
    ) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            LoginResponse loginResponse = userService.login(request, response);
            String token = jwtUtil.generateToken(request.getUsername(),loginResponse);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Login Succesfully, Welcome.");
            response.setData(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.setMessage("Username or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BadCredentialsException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.FORBIDDEN);
            response.setMessage("Username or password is incorrect");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            userService.register(request, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Register Succesfully, Welcome.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPassword) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            userService.resetPassword(resetPassword, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Reset Password Succesfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/")
    public ResponseEntity editUser(@RequestBody EditUserRequest request) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            userService.editUser(request, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Edit User Succesfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity deleteUser(@PathVariable String username) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            userService.deleteUser(username, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Delete User Succesfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PatchMapping("/{username}")
    public ResponseEntity inactiveUser(
            @PathVariable String username,
            @RequestParam(value = "is_active", defaultValue = "false") Boolean isActive
    ) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            userService.changeStatusUser(username, isActive, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setMessage("Edit Status User Succesfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/")
    public ResponseEntity getAllUserList(
         @RequestParam(value = "page", defaultValue = "0") int page,
         @RequestParam(value = "limit", defaultValue = "10") int limit,
         @RequestParam(value = "sort_by", defaultValue = "created_at") String sortBy,
         @RequestParam(value = "order", defaultValue = "false") Boolean order,
         @RequestParam(value = "search", defaultValue = "", required = false) String search
    ) {
        BaseResponse<Page<UserListResponse>> response = new BaseResponse<>();
        try {
            Page<UserListResponse> userListResponse = userService.showListUser(search, page, limit, sortBy, order);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setData(userListResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.getErrorMessages().add(BaseErrorMessage.builder().field("List User").message(e.getMessage()).build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity getdetailUser(
            @PathVariable String username
    ) {
        BaseResponse<UserDetailResponse> response = new BaseResponse<>();
        try {
            UserDetailResponse user = userService.showDetailUser(username, response);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setData(user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.getErrorMessages().add(BaseErrorMessage.builder().field("List User").message(e.getMessage()).build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
