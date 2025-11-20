package com.javaapirestgosse.dto;

import com.javaapirestgosse.model.Address;
import java.util.Set;

public record AccountResponse(
    Long accountId,
    String username,
    String email,
    Address address,
    Set<String> roles
) {}
