package com.yogesh.employee_management_system.dto.leavetype;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveTypeRequest {

    @NotBlank(message = "Leave type name is required")
    @Size(min = 2, max = 100, message = "Leave type name must be between 2 and 100 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Min(value= 1, message = "Maximum leave days must be at least 1")
    private Integer maxDays;

}
