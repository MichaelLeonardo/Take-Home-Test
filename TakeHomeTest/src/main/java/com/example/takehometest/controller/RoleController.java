package com.example.takehometest.controller;

import com.example.takehometest._enum.ResponseCodeEnum;
import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import com.example.takehometest.dto.request.RoleAddEditRequest;
import com.example.takehometest.dto.response.RoleFeatureResponse;
import com.example.takehometest.dto.response.RoleListResponse;
import com.example.takehometest.service.RoleService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleService roleService;

    @PostMapping("/")
    public ResponseEntity addRoleFeature(
            @RequestBody RoleAddEditRequest request
    ) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            roleService.addRoleFeature(request,response);
            response.setStatusCodeByEnum(ResponseCodeEnum.CREATED);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.getErrorMessages().add(BaseErrorMessage.builder().field("Insert Role").message(e.getMessage()).build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity aditRoleFeature(
            @PathVariable Long id,
            @RequestBody RoleAddEditRequest request
    ) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            roleService.editRoleFeature(id, request,response);
            response.setStatusCodeByEnum(ResponseCodeEnum.CREATED);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.getErrorMessages().add(BaseErrorMessage.builder().field("Edit Role").message(e.getMessage()).build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/")
    public ResponseEntity getAllRoleFeature(
             @RequestParam(value = "page", defaultValue = "0") int page,
             @RequestParam(value = "limit", defaultValue = "10") int limit,
             @RequestParam(value = "sort_by", defaultValue = "created_at") String sortBy,
             @RequestParam(value = "order", defaultValue = "false") Boolean order,
             @RequestParam(value = "search", defaultValue = "", required = false) String search
    ) {
        BaseResponse<Page<RoleListResponse>> response = new BaseResponse<>();
        try {
            Page<RoleListResponse> roleFeatureResponse = roleService.getAllRoleFeatureResponse(search, page, limit, sortBy, order);
            response.setStatusCodeByEnum(ResponseCodeEnum.OK);
            response.setData(roleFeatureResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ValidationException e) {
            response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);
            response.getErrorMessages().add(BaseErrorMessage.builder().field("Insert Role").message(e.getMessage()).build());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
