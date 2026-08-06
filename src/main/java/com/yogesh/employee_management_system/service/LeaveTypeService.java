package com.yogesh.employee_management_system.service;

import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeRequest;
import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeResponse;
import org.springframework.data.domain.Page;

public interface LeaveTypeService {

    LeaveTypeResponse createLeaveType(LeaveTypeRequest request);

    Page<LeaveTypeResponse> getAllLeaveTypes(
            int page,
            int size,
            String sortBy,
            String direction
    );

    LeaveTypeResponse getLeaveTypeById(Long id);

    LeaveTypeResponse updateLeaveType(
            Long id,
            LeaveTypeRequest request
    );

    void deleteLeaveType(Long id);

    Page<LeaveTypeResponse> searchLeaveTypes(
            String keyword,
            int page,
            int size
    );

}
