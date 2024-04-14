package com.azry.library_management_system.controller;

import com.azry.library_management_system.dto.AuthRequestDTO;
import com.azry.library_management_system.dto.AuthResponseDTO;
import com.azry.library_management_system.model.ApplicationUser;
import com.azry.library_management_system.rate_limiting.RateLimitProtection;
import com.azry.library_management_system.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RateLimitProtection
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    public ResponseEntity<ApplicationUser> registerUser(@Valid @RequestBody AuthRequestDTO body) {
        ApplicationUser user = authService.registerUser(body.getUsername(), body.getPassword());
        return ResponseEntity.ok(user);
    }

    @RateLimitProtection
    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody AuthRequestDTO body) {
        AuthResponseDTO responseDTO = authService.loginUser(body.getUsername(), body.getPassword());
        return ResponseEntity.ok(responseDTO);
    }
}