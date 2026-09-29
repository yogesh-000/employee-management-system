package com.yogesh.employee_management_system.controller;

import com.yogesh.employee_management_system.dto.common.ApiResponse;
import com.yogesh.employee_management_system.dto.department.DepartmentRequest;
import com.yogesh.employee_management_system.dto.department.DepartmentResponse;
import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import com.yogesh.employee_management_system.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(
        name = "Department Management",
        description = "APIs for managing departments"
)
@RequiredArgsConstructor
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DepartmentResponse createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        return departmentService.createDepartment(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ApiResponse<Page<DepartmentResponse>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        logger.info("Recieved request to fetch departments. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Page<DepartmentResponse> departments = departmentService.getAllDepartments(
                page,
                size,
                sortBy,
                direction
        );

        return ApiResponse.<Page<DepartmentResponse>>builder()
                .success(true)
                .message("Departments fetched successfully.")
                .data(departments)
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/search")
    public ApiResponse<Page<DepartmentResponse>> searchDepartments(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        logger.info("Searching departments. Keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Page<DepartmentResponse> departments = departmentService.searchDepartments(
                keyword,
                page,
                size
        );

        return ApiResponse.<Page<DepartmentResponse>>builder()
                .success(true)
                .message("Departments fetched successfully.")
                .data(departments)
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public DepartmentResponse getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public DepartmentResponse updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request
    ) {
        return departmentService.updateDepartment(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

}
