package com.yogesh.employee_management_system.service;

import com.yogesh.employee_management_system.dto.auth.LoginRequest;
import com.yogesh.employee_management_system.dto.auth.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
}
