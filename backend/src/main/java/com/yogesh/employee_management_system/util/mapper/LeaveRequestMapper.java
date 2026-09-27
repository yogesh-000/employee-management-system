package com.yogesh.employee_management_system.util.mapper;

import com.yogesh.employee_management_system.dto.leaverequest.ApplyLeaveRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveRequestResponse;
import com.yogesh.employee_management_system.entity.Employee;
import com.yogesh.employee_management_system.entity.LeaveRequest;
import com.yogesh.employee_management_system.entity.LeaveType;

public class LeaveRequestMapper {

    private LeaveRequestMapper() {

    }

    public static LeaveRequest toEntity(
            ApplyLeaveRequest request,
            Employee employee,
            LeaveType leaveType
    ) {
        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);

        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());

        return leaveRequest;
    }

    public static LeaveRequestResponse toResponse(LeaveRequest leaveRequest) {

        return LeaveRequestResponse.builder()
                .id(leaveRequest.getId())
                .employeeName(
                        leaveRequest.getEmployee().getFirstName()+ " "+
                                leaveRequest.getEmployee().getLastName()
                ).leaveType(
                        leaveRequest.getLeaveType().getName()
                )
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .appliedAt(leaveRequest.getAppliedAt())
                .approvedBy(leaveRequest.getApprovedBy() == null ? null
                        : leaveRequest.getApprovedBy().getFirstName()+ " "+
                        leaveRequest.getApprovedBy().getLastName())
                .remarks(leaveRequest.getRemarks())
                .build();
    }
}
