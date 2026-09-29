package com.yogesh.employee_management_system.service;

import com.yogesh.employee_management_system.dto.employee.EmployeeRequest;
import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import com.yogesh.employee_management_system.dto.employee.EmployeeUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    Page<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<EmployeeResponse> searchEmployees(
            String keyword,
            int page,
            int size
    );

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void deleteEmployee(Long id);

}
