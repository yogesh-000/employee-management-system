package com.yogesh.employee_management_system.dto.employee;

import com.yogesh.employee_management_system.enums.EmployeeStatus;
import com.yogesh.employee_management_system.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EmployeeResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Gender gender;

    private String designation;

    private BigDecimal salary;

    private LocalDate joiningDate;

    private EmployeeStatus status;

    private String department;

    private String role;

    private String manager;

    private String username;

}
