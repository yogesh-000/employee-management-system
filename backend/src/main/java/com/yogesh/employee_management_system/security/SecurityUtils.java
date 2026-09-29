package com.yogesh.employee_management_system.security;

import com.yogesh.employee_management_system.entity.Employee;
import com.yogesh.employee_management_system.exception.ResourceNotFoundException;
import com.yogesh.employee_management_system.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final EmployeeRepository employeeRepository;

    public Employee getCurrentEmployee() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return employeeRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Logged-in user has no linked employee record."));
    }

    public boolean currentUserHasRole(String role) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_" + role));
    }
}