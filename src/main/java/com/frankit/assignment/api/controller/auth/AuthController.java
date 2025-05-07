package com.frankit.assignment.api.controller.auth;

import com.frankit.assignment.api.controller.auth.request.LoginRequest;
import com.frankit.assignment.api.controller.auth.request.SignupRequest;
import com.frankit.assignment.api.service.auth.AuthService;
import com.frankit.assignment.api.service.auth.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        AuthResponse result = authService.signup(signupRequest.toServiceDto());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthResponse result = authService.login(loginRequest.toServiceDto());
        return ResponseEntity.ok(result);
    }

}
