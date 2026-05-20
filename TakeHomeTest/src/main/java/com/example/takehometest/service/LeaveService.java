package com.example.takehometest.service;

import com.example.takehometest._enum.LeaveStatusEnum;
import com.example.takehometest.dto.response.LeaveListResponse;
import com.example.takehometest.dto.response.RoleListResponse;
import com.example.takehometest.model.Leave;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.User;
import com.example.takehometest.repository.LeaveRepository;
import com.example.takehometest.repository.UserRepository;
import com.example.takehometest.specification.LeaveSpecification;
import com.example.takehometest.utils.DateUtil;
import com.example.takehometest.validation.LeaveValidation;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private LeaveValidation leaveValidation;
    @Autowired
    private LeaveSpecification leaveSpecification;

    @Transactional(rollbackOn = { Exception.class })
    public void applyLeave (Date dateFrom, Date dateTo, String reason) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        User user =  userOpt.get();

        // Validate Request
        leaveValidation.applyRequestValidation(dateFrom,dateTo, userOpt, reason);

        // Count days leave
        LocalDate startDate = DateUtil.convertToLocalDate(dateFrom);
        LocalDate endDate = DateUtil.convertToLocalDate(dateTo);
        Long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Insert data into leave table
        Leave leave =  new Leave();
        leave.setDateFrom(dateFrom);
        leave.setDateTo(dateTo);
        leave.setUser(user);
        leave.setTotalDays(Integer.parseInt(String.valueOf(days)));
        leave.setLeaveStatus(user.getRole().getIsAdmin() ? LeaveStatusEnum.APPROVED.getId() : LeaveStatusEnum.PENDING.getId());
        leaveRepository.save(leave);

        // Decrease user days leave
        user.setRemainingDaysOff(user.getRemainingDaysOff() - Integer.parseInt(String.valueOf(days)));
        userRepository.save(user);
    }

    public Page<LeaveListResponse> getAllHistoryPersonalLeave (
            Integer page, Integer limit, String orderBy, Boolean asc,
            Date dateFrom, Date dateTo, Integer status
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        if (userOpt.isEmpty()) {
            throw new ValidationException("User doesn't exists");
        }

        Pageable pageable = PageRequest.of(page, limit);
        Page<Leave> leavePage = leaveRepository.findAll(
                leaveSpecification.sortBy(orderBy,asc)
                        .and(leaveSpecification.findByRangeDates(dateFrom, dateTo))
                        .and(leaveSpecification.findByStatus(status))
                        .and(leaveSpecification.findByRequester(userOpt.get()))
                , pageable);
        List<LeaveListResponse> allHistoryPersonalLeave = mappingAllHistoryPersonalLeave(leavePage.getContent());

        return new PageImpl<>(allHistoryPersonalLeave, pageable, leavePage.getTotalElements());
    }

    private List<LeaveListResponse> mappingAllHistoryPersonalLeave (List<Leave> leave) {
        List<LeaveListResponse> listResponses = new ArrayList<>();

        for(Leave leaveResp : leave){
            LocalDate startDate = DateUtil.convertToLocalDate(leaveResp.getDateFrom());
            LocalDate endDate = DateUtil.convertToLocalDate(leaveResp.getDateTo());
            listResponses.add(new LeaveListResponse(leaveResp.getId(), startDate,endDate,leaveResp.getTotalDays(),LeaveStatusEnum.getById(leaveResp.getLeaveStatus()).getName(), leaveResp.getUser().getFullName()));
        }

        return listResponses;
    }

    public void approveLeave (Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        Optional<Leave> leaveOptional = leaveRepository.findFirstByIdAndLeaveStatus(id, LeaveStatusEnum.PENDING.getId());

        // Validate Request
        leaveValidation.approveLeaveValidation(leaveOptional, userOpt);

        // Update data into leave table
        Leave leave =  leaveOptional.get();
        leave.setLeaveStatus(LeaveStatusEnum.APPROVED.getId());
        leaveRepository.save(leave);
    }

    public Page<LeaveListResponse> getAllHistoryLeave (
            Integer page, Integer limit, String orderBy, Boolean asc,
            Date dateFrom, Date dateTo, Integer status, Long idEmployee
    ) {

        User user = null;
        if (idEmployee != null) {
            Optional<User> userOpt = userRepository.findByIdAndIsDeletedIsFalse(idEmployee);
            if (userOpt.isEmpty()) {
                throw new ValidationException("User doesn't exists");
            }
            user = userOpt.get();
        }

        Pageable pageable = PageRequest.of(page, limit);
        Page<Leave> leavePage = leaveRepository.findAll(
                leaveSpecification.sortBy(orderBy,asc)
                        .and(leaveSpecification.findByRangeDates(dateFrom, dateTo))
                        .and(leaveSpecification.findByStatus(status))
                        .and(leaveSpecification.findByRequester(user))
                , pageable);
        List<LeaveListResponse> allHistoryPersonalLeave = mappingAllHistoryPersonalLeave(leavePage.getContent());

        return new PageImpl<>(allHistoryPersonalLeave, pageable, leavePage.getTotalElements());
    }

    public void rejectedLeave (Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedIsFalse(username);
        Optional<Leave> leaveOptional = leaveRepository.findFirstByIdAndLeaveStatus(id, LeaveStatusEnum.PENDING.getId());

        // Validate Request
        leaveValidation.rejectLeaveRequestValidation(leaveOptional, userOpt);

        // Update data into leave table
        Leave leave =  leaveOptional.get();
        leave.setLeaveStatus(LeaveStatusEnum.REJECTED.getId());
        leaveRepository.save(leave);
    }
}
