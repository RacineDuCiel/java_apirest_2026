package com.javaapirestgosse.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Schema(example = "john.doe")
    String username,
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(example = "password123")
    String password
) {}
