package com.yogesh.employee_management_system.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String username;
    private String role;

}
