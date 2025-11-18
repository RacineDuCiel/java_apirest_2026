package com.javaapirestgosse.security;

import com.javaapirestgosse.model.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AccountSecurity {

    public boolean canAccessAccount(Authentication authentication, Long accountId) {
        if (authentication == null || accountId == null) {
            return false;
        }

        if (isAdmin(authentication)) {
            return true;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Account account) {
            return accountId.equals(account.getAccountId());
        }

        return false;
    }

    private boolean isAdmin(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
