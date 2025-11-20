package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Schema(example = "john.doe")
    String username,
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(example = "john.doe@example.com")
    String email,
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(example = "password123")
    String password,
    
    @jakarta.validation.Valid
    Address address
) {}
