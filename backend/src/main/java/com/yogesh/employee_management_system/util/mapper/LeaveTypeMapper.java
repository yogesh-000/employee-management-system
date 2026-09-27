package com.yogesh.employee_management_system.util.mapper;

import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeRequest;
import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeResponse;
import com.yogesh.employee_management_system.entity.LeaveType;

public class LeaveTypeMapper {

    private LeaveTypeMapper(){

    }

    public static LeaveType toEntity(LeaveTypeRequest request) {

        LeaveType leaveType = new LeaveType();

        leaveType.setName(request.getName());
        leaveType.setDescription(request.getDescription());
        leaveType.setMaxDays(request.getMaxDays());

        return leaveType;
    }

    public static LeaveTypeResponse toResponse(LeaveType leaveType) {

        return LeaveTypeResponse.builder()
                .id(leaveType.getId())
                .name(leaveType.getName())
                .description(leaveType.getDescription())
                .maxDays(leaveType.getMaxDays())
                .build();
    }

    public static void updateEntity(
            LeaveType leaveType,
            LeaveTypeRequest request
    ) {
        leaveType.setName(request.getName());
        leaveType.setDescription(request.getDescription());
        leaveType.setMaxDays(request.getMaxDays());
    }

}
