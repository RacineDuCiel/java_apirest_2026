package com.javaapirestgosse.service;

import com.javaapirestgosse.dto.AuthResponse;
import com.javaapirestgosse.dto.LoginRequest;
import com.javaapirestgosse.dto.RegisterRequest;
import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public AuthService(AccountRepository accountRepository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       AccountService accountService) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Créer le compte via AccountService qui gère la validation et le hachage
        Account account = new Account();
        account.setUsername(request.username());
        account.setEmail(request.email());
        account.setPassword(request.password());
        account.setAddress(request.address());

        Account savedAccount = accountService.createAccount(account);

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(savedAccount);

        return new AuthResponse(jwtToken, savedAccount.getUsername(), savedAccount.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // Authentifier l'utilisateur
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Récupérer l'utilisateur
        Account account = accountRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Connexion impossible : aucun compte n'est associé au nom d'utilisateur '" + request.username() + "'."));

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(account);

        return new AuthResponse(jwtToken, account.getUsername(), account.getEmail());
    }
}
