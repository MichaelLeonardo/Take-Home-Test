package com.example.takehometest.controller;

import com.example.takehometest._enum.ResponseCodeEnum;
import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import com.example.takehometest.dto.response.LeaveListResponse;
import com.example.takehometest.dto.response.LoginResponse;
import com.example.takehometest.dto.response.UserListResponse;
import com.example.takehometest.service.LeaveService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/request/apply")
    private ResponseEntity<?> applyLeaveRequest (
            @RequestParam(value = "date_from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam(value = "date_to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "reason") String reason
    ) {
        BaseResponse<String> response = new BaseResponse<>();

        leaveService.applyLeave(dateFrom, dateTo, reason);

        response.setStatusCodeByEnum(ResponseCodeEnum.CREATED);
        response.setData("Leave request submitted");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/request/apply")
    private ResponseEntity<?> applyLeaveHistory (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(value = "order", defaultValue = "false") Boolean order,
            @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        BaseResponse<Page<LeaveListResponse>> response = new BaseResponse<>();

        Page<LeaveListResponse> leaveListResponse = leaveService.getAllHistoryPersonalLeave(
                page, limit, sortBy, order, dateFrom, dateTo, status
        );

        response.setStatusCodeByEnum(ResponseCodeEnum.CREATED);
        response.setData(leaveListResponse);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @PostMapping("/approval/apply/{id}")
    private ResponseEntity<?> approveLeaveRequest (
            @PathVariable Long id
    ) {
        BaseResponse<String> response = new BaseResponse<>();

        leaveService.approveLeave(id);

        response.setStatusCodeByEnum(ResponseCodeEnum.UPDATED);
        response.setData("Leave request approved");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/approval/apply")
    private ResponseEntity<?> allLeaveHistory (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(value = "order", defaultValue = "false") Boolean order,
            @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "employee_id", required = false) Long employeeId
    ) {
        BaseResponse<Page<LeaveListResponse>> response = new BaseResponse<>();

        Page<LeaveListResponse> leaveListResponse = leaveService.getAllHistoryLeave(
                page, limit, sortBy, order, dateFrom, dateTo, status, employeeId
        );

        response.setStatusCodeByEnum(ResponseCodeEnum.CREATED);
        response.setData(leaveListResponse);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @PostMapping("/approval/apply/reject/{id}")
    private ResponseEntity<?> rejectLeaveRequest (
            @PathVariable Long id
    ) {
        BaseResponse<String> response = new BaseResponse<>();

        leaveService.rejectedLeave(id);

        response.setStatusCodeByEnum(ResponseCodeEnum.UPDATED);
        response.setData("Leave request rejected");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }
}
