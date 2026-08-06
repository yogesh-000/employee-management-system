package com.yogesh.employee_management_system.controller;

import com.yogesh.employee_management_system.dto.auth.LoginRequest;
import com.yogesh.employee_management_system.dto.auth.LoginResponse;
import com.yogesh.employee_management_system.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication",
        description = "Authentication APIs"
)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
            ) {
        return new ResponseEntity<>(
                authenticationService.login(request),
                HttpStatus.OK
        );
    }
}
