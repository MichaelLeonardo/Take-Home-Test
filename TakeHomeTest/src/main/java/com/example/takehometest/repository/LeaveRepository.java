package com.example.takehometest.repository;


import com.example.takehometest.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave,Long>, JpaSpecificationExecutor<Leave> {

    Optional<Leave> findFirstByDateToGreaterThanEqualAndLeaveStatusIsNot(Date dateFrom, Integer cancel);

    Optional<Leave> findFirstByIdAndLeaveStatus(Long id, Integer pending);
}
