package com.yogesh.employee_management_system.dto.leaverequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LeaveHistoryResponse {

    private Long employeeId;

    private String employeeName;

    private List<LeaveRequestResponse> leaveRequests;

}
