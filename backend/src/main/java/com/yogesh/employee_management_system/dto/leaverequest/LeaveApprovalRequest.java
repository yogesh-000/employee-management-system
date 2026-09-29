package com.yogesh.employee_management_system.dto.leaverequest;

import com.yogesh.employee_management_system.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveApprovalRequest {

    @NotNull(message = "Status is required")
    private LeaveStatus status;

    @Size(max = 255, message = "Remarks cannot exceed 255 characters")
    private String remarks;

}
