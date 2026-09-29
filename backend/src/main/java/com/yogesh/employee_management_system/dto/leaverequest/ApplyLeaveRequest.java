package com.yogesh.employee_management_system.dto.leaverequest;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ApplyLeaveRequest {

    @NotNull(message = "Leave type is required")
    private Long leaveTypeId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in past")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    @Size(
            min = 10,
            max = 500,
            message = "Reason must be between 10 and 500 characters"
    )
    private String reason;

}
