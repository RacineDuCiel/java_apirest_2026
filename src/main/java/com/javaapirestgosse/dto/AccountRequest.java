package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import lombok.Data;

@Data
public class AccountRequest {
    private String username;
    private String email;
    private String password;
    private Address address;
}
