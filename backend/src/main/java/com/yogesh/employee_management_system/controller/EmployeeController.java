package com.yogesh.employee_management_system.controller;

import com.yogesh.employee_management_system.dto.common.ApiResponse;
import com.yogesh.employee_management_system.dto.employee.EmployeeRequest;
import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import com.yogesh.employee_management_system.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(
        name = "Employee Management",
        description = "APIs for managing employees"
)
@RequiredArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    @PostMapping
    public ApiResponse<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);

        return ApiResponse
                .<EmployeeResponse>builder()
                .success(true)
                .message("Employee created successfully.")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        logger.info("Recieved request to fetch employees. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Page<EmployeeResponse> employees = employeeService.getAllEmployees(
                page,
                size,
                sortBy,
                direction
        );

        return ApiResponse.<Page<EmployeeResponse>>builder()
                .success(true)
                .message("Employees fetched successfully.")
                .data(employees)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        logger.info("Searching employees. Keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Page<EmployeeResponse> employees = employeeService.searchEmployees(
                keyword,
                page,
                size
        );

        return ApiResponse.<Page<EmployeeResponse>>builder()
                .success(true)
                .message("Employees fetched successfully.")
                .data(employees)
                .build();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}
