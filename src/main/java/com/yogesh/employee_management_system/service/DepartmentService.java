package com.yogesh.employee_management_system.service;


import com.yogesh.employee_management_system.dto.department.DepartmentRequest;
import com.yogesh.employee_management_system.dto.department.DepartmentResponse;
import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    Page<DepartmentResponse> getAllDepartments(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<DepartmentResponse> searchDepartments(
            String keyword,
            int page,
            int size
    );

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);

}
