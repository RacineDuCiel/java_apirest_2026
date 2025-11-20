package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record AccountRequest(
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Schema(example = "user_created_by_admin")
    String username,
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(example = "user_created_by_admin@example.com")
    String email,
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(example = "user_created_by_admin")
    String password,
    
    Address address
) {}
