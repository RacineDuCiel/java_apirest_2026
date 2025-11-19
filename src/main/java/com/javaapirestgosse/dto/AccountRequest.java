package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import lombok.Data;

@Data
public class AccountRequest {
    @io.swagger.v3.oas.annotations.media.Schema(example = "user_created_by_admin")
    private String username;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "user_created_by_admin@example.com")
    private String email;
    
    @io.swagger.v3.oas.annotations.media.Schema(example = "user_created_by_admin")
    private String password;
    
    private Address address;
}
