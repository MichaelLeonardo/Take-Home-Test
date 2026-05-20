package com.example.takehometest.validation;

import com.example.takehometest._enum.LeaveStatusEnum;
import com.example.takehometest.model.Leave;
import com.example.takehometest.model.User;
import com.example.takehometest.repository.LeaveRepository;
import com.example.takehometest.utils.DateUtil;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class LeaveValidation {

    @Autowired
    private LeaveRepository leaveRepository;

    public void applyRequestValidation (Date dateFrom, Date dateTo, Optional<User> user, String reason) {
        if (reason.isEmpty() || dateTo == null || dateFrom == null){
            throw new ValidationException("Please fill up all field above");
        }

        if (user.isEmpty()) {
            throw new ValidationException("User doesn't exists");
        }

        // Checking is date from later than date to
        if (dateFrom.after(dateTo)) {
            throw new ValidationException("Start date cannot be later than end date.");
        }

        LocalDate startDate = DateUtil.convertToLocalDate(dateFrom);
        LocalDate endDate = DateUtil.convertToLocalDate(dateTo);

        // Count days leave
        Long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (user.get().getRemainingDaysOff() < days) {
            throw new ValidationException("Insufficient remaining leave days.");
        }

        // Check Overlap
        Optional <Leave> overlapLeave = leaveRepository.findFirstByDateToGreaterThanEqualAndLeaveStatusIsNot(dateFrom, LeaveStatusEnum.REJECTED.getId());
        if (overlapLeave.isPresent()) {
            throw new ValidationException("You cannot overlap your leave date.");
        }
    }

    public void approveLeaveValidation (Optional<Leave> leaveOpt, Optional<User> user) {
        if (leaveOpt.isEmpty()) {
            throw new ValidationException("Leave data doesn't exists");
        }
        if (user.isEmpty()) {
            throw new ValidationException("User doesn't exists");
        }

        Leave leave = leaveOpt.get();
        // Checking is date from later than date to
        if (!LeaveStatusEnum.PENDING.getId().equals(leave.getLeaveStatus())) {
            throw new ValidationException("Cannot approve leave status not in pending.");
        }

        User auth = user.get();
        // if manager apply leave, manager cannot approve. who can approve just superadmin.
        if (auth.getRole().getRoleName().equals("Manager") && !leave.getUser().getRole().getRoleName().equals("Employee")) {
            throw new ValidationException("Manager just can approve employee leave request.");
        }
        if(leave.getUser().equals(auth)) {
            throw new ValidationException("Cannot approve your own apply.");
        }

        if (auth.getRole().getRoleName().equals("Employee")) {
            throw new ValidationException("Employees are not allowed to approve leave requests.");
        }

    }

    public void rejectLeaveRequestValidation (Optional<Leave> leaveOpt, Optional<User> user) {
        if (leaveOpt.isEmpty()) {
            throw new ValidationException("Leave data doesn't exists");
        }
        if (user.isEmpty()) {
            throw new ValidationException("User doesn't exists");
        }

        Leave leave = leaveOpt.get();
        // Checking is date from later than date to
        if (!LeaveStatusEnum.APPROVED.getId().equals(leave.getLeaveStatus())) {
            throw new ValidationException("Approved request cannot be rejected.");
        }
    }
}
