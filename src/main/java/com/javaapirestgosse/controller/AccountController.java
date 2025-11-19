package com.javaapirestgosse.controller;

import com.javaapirestgosse.dto.AccountRequest;
import com.javaapirestgosse.dto.AccountResponse;
import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.Role;
import com.javaapirestgosse.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Comptes", description = "Gestion des comptes utilisateurs")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Lister tous les comptes",
        description = "Réservé aux administrateurs.",
        security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    @Operation(summary = "Consulter un compte",
        description = "Accessible à l'administrateur ou au propriétaire du compte.",
        security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(mapToResponse(account));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Créer un compte",
        description = "Réservé aux administrateurs. Pour l'inscription utilisateur, utiliser /api/auth/register.",
        security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = mapToEntity(request);
        Account savedAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedAccount));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    @Operation(summary = "Mettre à jour un compte",
        description = "Accessible à l'administrateur ou au propriétaire du compte.",
        security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequest request) {
        Account account = mapToEntity(request);
        Account updatedAccount = accountService.updateAccount(id, account);
        return ResponseEntity.ok(mapToResponse(updatedAccount));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    @Operation(summary = "Supprimer un compte",
        description = "Accessible à l'administrateur ou au propriétaire du compte.",
        security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Compte supprimé avec succès");
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setUsername(account.getUsername());
        response.setEmail(account.getEmail());
        response.setAddress(account.getAddress());
        response.setRoles(account.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return response;
    }

    private Account mapToEntity(AccountRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setAddress(request.getAddress());
        return account;
    }
}
