package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AddressValidationService addressValidationService;

    public AccountService(AccountRepository accountRepository,
                          AddressValidationService addressValidationService) {
        this.accountRepository = accountRepository;
        this.addressValidationService = addressValidationService;
    }

    public Account createAccount(Account account) {
        if (account.getAddress() != null) {
            boolean isValid = addressValidationService.validateAddress(account.getAddress());
            if (!isValid) {
                throw new IllegalArgumentException("L'adresse fournie n'est pas valide");
            }
        }
        return accountRepository.save(account);
    }
}
