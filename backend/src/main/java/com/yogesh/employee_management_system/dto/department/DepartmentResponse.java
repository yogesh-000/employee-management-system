package com.yogesh.employee_management_system.dto.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;

    private String name;

    private String description;

}
