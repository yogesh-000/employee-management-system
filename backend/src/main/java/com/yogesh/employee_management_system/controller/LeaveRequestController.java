package com.yogesh.employee_management_system.controller;

import com.yogesh.employee_management_system.dto.leaverequest.ApplyLeaveRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveApprovalRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveHistoryResponse;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveRequestResponse;
import com.yogesh.employee_management_system.enums.LeaveStatus;
import com.yogesh.employee_management_system.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-requests")
@Tag(
        name = "Leave Management",
        description = "APIs for leave requests"
)
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PostMapping
    public ResponseEntity<LeaveRequestResponse> applyLeave(
            @Valid @RequestBody ApplyLeaveRequest request) {

        LeaveRequestResponse response =
                leaveRequestService.applyLeave(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LeaveRequestResponse>> getByStatus(
            @PathVariable LeaveStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByStatus(status, page, size));
    }

    @GetMapping("/my")
    public ResponseEntity<LeaveHistoryResponse> getMyLeaveHistory() {
        return ResponseEntity.ok(leaveRequestService.getMyLeaveHistory());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<Page<LeaveRequestResponse>> getAllLeaveRequests(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "appliedAt") String sortBy,

            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(

                leaveRequestService.getAllLeaveRequests(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestResponse> getLeaveRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveRequestService.getLeaveRequestById(id)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<LeaveHistoryResponse> getEmployeeLeaveHistory(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(

                leaveRequestService.getEmployeeLeaveHistory(employeeId)

        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{leaveRequestId}/approve")
    public ResponseEntity<LeaveRequestResponse> approveOrRejectLeave(

            @PathVariable Long leaveRequestId,

            @Valid
            @RequestBody
            LeaveApprovalRequest request) {

        return ResponseEntity.ok(

                leaveRequestService.approveOrRejectLeave(
                        leaveRequestId,
                        request
                )

        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PutMapping("/{leaveRequestId}/cancel")
    public ResponseEntity<LeaveRequestResponse> cancelLeave(

            @PathVariable Long leaveRequestId) {

        return ResponseEntity.ok(

                leaveRequestService.cancelLeave(
                        leaveRequestId
                )

        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<Page<LeaveRequestResponse>> searchLeaveRequests(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(

                leaveRequestService.searchLeaveRequests(
                        keyword,
                        page,
                        size
                )

        );
    }

}
