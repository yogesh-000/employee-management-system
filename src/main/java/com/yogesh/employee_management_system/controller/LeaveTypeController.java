package com.yogesh.employee_management_system.controller;

import com.yogesh.employee_management_system.dto.common.ApiResponse;
import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeRequest;
import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeResponse;
import com.yogesh.employee_management_system.service.LeaveTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/leave-types")
@Tag(
        name = "Leave Type Management",
        description = "APIs for managing leave types"
)
@RequiredArgsConstructor
public class LeaveTypeController {

    private static final Logger logger = LoggerFactory.getLogger(LeaveTypeController.class);

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public ApiResponse<LeaveTypeResponse> createLeaveType(
            @Valid @RequestBody LeaveTypeRequest request
            ) {
        logger.info("Received request to create leave type.");

        LeaveTypeResponse response = leaveTypeService.createLeaveType(request);

        return ApiResponse.<LeaveTypeResponse>builder()
                .success(true)
                .message("Leave type created successfully.")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<LeaveTypeResponse>> getAllLeaveTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        logger.info("Received request to fetch leave types. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Page<LeaveTypeResponse> response = leaveTypeService.getAllLeaveTypes(
                page,
                size,
                sortBy,
                direction
        );

        return ApiResponse.<Page<LeaveTypeResponse>>builder()
                .success(true)
                .message("Leave types fetched successfully.")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LeaveTypeResponse> getLeaveTypeById(
            @PathVariable Long id
    ) {
        logger.info("Received request to fetch leave type with ID: {}", id);

        LeaveTypeResponse response = leaveTypeService.getLeaveTypeById(id);

        return ApiResponse.<LeaveTypeResponse>builder()
                .success(true)
                .message("Leave type fetched successfully.")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<LeaveTypeResponse> updateLeaveType(
            @PathVariable Long id,
            @Valid @RequestBody LeaveTypeRequest request
    ) {
        logger.info("Received request update leave type with ID: {}", id);

        LeaveTypeResponse response = leaveTypeService.updateLeaveType(id, request);

        return ApiResponse.<LeaveTypeResponse>builder()
                .success(true)
                .message("Leave type updated successfully.")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLeaveType(
            @PathVariable Long id
    ) {
        logger.info("Received request to delete leave type with ID: {}", id);

        leaveTypeService.deleteLeaveType(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Leave type deleted successfully.")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<LeaveTypeResponse>> searchLeaveTypes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("Received request to search leave types. Keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Page<LeaveTypeResponse> response = leaveTypeService.searchLeaveTypes(
                keyword,
                page,
                size
        );

        return ApiResponse.<Page<LeaveTypeResponse>>builder()
                .success(true)
                .message("Leave types fetched successfully.")
                .data(response)
                .build();
    }

}
