package com.yogesh.employee_management_system.service;

import com.yogesh.employee_management_system.dto.leaverequest.ApplyLeaveRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveApprovalRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveHistoryResponse;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveRequestResponse;
import org.springframework.data.domain.Page;

public interface LeaveRequestService {

    LeaveRequestResponse applyLeave(
            ApplyLeaveRequest request
    );

    Page<LeaveRequestResponse> getAllLeaveRequests(
            int page,
            int size,
            String sortBy,
            String direction
    );

    LeaveRequestResponse getLeaveRequestById(Long id);

    LeaveHistoryResponse getEmployeeLeaveHistory(Long employeeId);

    LeaveRequestResponse approveOrRejectLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request
    );

    LeaveRequestResponse cancelLeave(Long leaveRequestId);

    Page<LeaveRequestResponse> searchLeaveRequests(
            String keyword,
            int page,
            int size
    );
}
