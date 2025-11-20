package com.javaapirestgosse.controller;

import com.javaapirestgosse.dto.AuthResponse;
import com.javaapirestgosse.dto.LoginRequest;
import com.javaapirestgosse.dto.RegisterRequest;
import com.javaapirestgosse.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Inscription et connexion (routes publiques)")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscrire un utilisateur",
        description = "Route publique. Aucun token requis. Retourne un JWT." )
    public ResponseEntity<AuthResponse> register(@jakarta.validation.Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur",
        description = "Route publique. Fournit un JWT en cas de succès." )
    public ResponseEntity<AuthResponse> login(@jakarta.validation.Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
