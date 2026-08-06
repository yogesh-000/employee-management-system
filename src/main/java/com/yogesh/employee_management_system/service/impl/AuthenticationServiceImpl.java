package com.yogesh.employee_management_system.service.impl;

import com.yogesh.employee_management_system.dto.auth.LoginRequest;
import com.yogesh.employee_management_system.dto.auth.LoginResponse;
import com.yogesh.employee_management_system.security.JwtService;
import com.yogesh.employee_management_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login request received for username: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                request.getUsername()
        );

        String token = jwtService.generateToken(userDetails);

        log.info("JWT generated successfully for {}", request.getUsername());

        return new LoginResponse(token);
    }
}
