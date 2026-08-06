package com.yogesh.employee_management_system.util.mapper;

import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import com.yogesh.employee_management_system.entity.Employee;

public class EmployeeMapper {

    private EmployeeMapper(){

    }

    public static EmployeeResponse toResponse(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .gender(employee.getGender())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .status(employee.getStatus())
                .department(employee.getDepartment().getName())
                .role(employee.getUser().getRole().getName().name())
                .manager(employee.getManager() != null ?
                        employee.getManager().getFirstName()+ " "+
                        employee.getManager().getLastName()
                        : null)
                .username(employee.getUser().getUsername())
                .build();
    }
}
