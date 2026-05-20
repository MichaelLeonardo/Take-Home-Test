package com.example.takehometest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@Getter @Setter
public class LeaveListResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date_from")
    private LocalDate dateFrom;

    @JsonProperty("date_to")
    private LocalDate dateTo;

    @JsonProperty("total_days")
    private Integer totalDays;

    @JsonProperty("leave_status")
    private String leaveStatus;

    @JsonProperty("requester")
    private String requester;
}
