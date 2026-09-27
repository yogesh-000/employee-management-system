package com.yogesh.employee_management_system.dto.leaverequest;

import com.yogesh.employee_management_system.enums.LeaveStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LeaveRequestResponse {

    private Long id;

    private String employeeName;

    private String leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private LeaveStatus status;

    private LocalDateTime appliedAt;

    private String approvedBy;

    private String remarks;

}
