package com.yogesh.employee_management_system.dto.employee;

import com.yogesh.employee_management_system.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeUpdateRequest {
    @NotBlank @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank @Email
    private String email;
    @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;
    @NotNull
    private Gender gender;
    @NotBlank @Size(min = 2, max = 100)
    private String designation;
    @NotNull @Positive(message = "Salary must be greater than 0")
    private BigDecimal salary;
    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
    @NotNull @Min(1)
    private Long departmentId;
}
