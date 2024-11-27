package com.evolve.controller;

import com.evolve.dto.JwtAuthenticationDto;
import com.evolve.dto.RefreshTokenDto;
import com.evolve.dto.UserCredintialsDto;
import com.evolve.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@RequestBody UserCredintialsDto userCredintialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(userCredintialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationDto> register(@RequestBody UserCredintialsDto userCredintialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.signUp(userCredintialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}