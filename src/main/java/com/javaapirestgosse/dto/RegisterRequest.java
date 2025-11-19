package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import lombok.Data;

@Data
public class RegisterRequest {
    @io.swagger.v3.oas.annotations.media.Schema(example = "john.doe")
    private String username;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "john.doe@example.com")
    private String email;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "password123")
    private String password;
    
    private Address address;
}
