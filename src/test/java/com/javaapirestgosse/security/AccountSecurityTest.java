package com.javaapirestgosse.security;

import com.javaapirestgosse.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSecurityTest {

    private final AccountSecurity accountSecurity = new AccountSecurity();

    @Test
    void adminCanAccessAnyAccount() {
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken("admin", "password", "ROLE_ADMIN");

        boolean result = accountSecurity.canAccessAccount(authentication, 42L);

        assertThat(result).isTrue();
    }

    @Test
    void ownerCanAccessOwnAccount() {
        Account account = new Account();
        account.setAccountId(5L);
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(account, "password", "ROLE_USER");

        boolean result = accountSecurity.canAccessAccount(authentication, 5L);

        assertThat(result).isTrue();
    }

    @Test
    void userCannotAccessOtherAccount() {
        Account account = new Account();
        account.setAccountId(5L);
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(account, "password", "ROLE_USER");

        boolean result = accountSecurity.canAccessAccount(authentication, 8L);

        assertThat(result).isFalse();
    }

    @Test
    void nullAuthenticationDenied() {
        boolean result = accountSecurity.canAccessAccount(null, 1L);

        assertThat(result).isFalse();
    }
}
