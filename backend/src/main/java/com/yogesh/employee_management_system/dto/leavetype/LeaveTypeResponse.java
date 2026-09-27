package com.yogesh.employee_management_system.dto.leavetype;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveTypeResponse {

    private Long id;

    private String name;

    private String description;

    private Integer maxDays;

}
