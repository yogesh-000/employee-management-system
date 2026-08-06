package com.yogesh.employee_management_system.util.mapper;

import com.yogesh.employee_management_system.dto.department.DepartmentResponse;
import com.yogesh.employee_management_system.entity.Department;

public class DepartmentMapper {

    private DepartmentMapper() {

    }

    public static DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
