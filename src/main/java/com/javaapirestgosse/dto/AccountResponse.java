package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import lombok.Data;
import java.util.Set;

@Data
public class AccountResponse {
    private Long accountId;
    private String username;
    private String email;
    private Address address;
    private Set<String> roles;
}
