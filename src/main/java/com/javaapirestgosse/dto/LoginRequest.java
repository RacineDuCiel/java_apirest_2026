package com.javaapirestgosse.dto;

import lombok.Data;

@Data
public class LoginRequest {
    @io.swagger.v3.oas.annotations.media.Schema(example = "john.doe")
    private String username;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "password123")
    private String password;
}
