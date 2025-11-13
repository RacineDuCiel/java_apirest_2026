package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
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
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setAddress(request.getAddress());

        Account savedAccount = accountService.createAccount(account);

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(savedAccount);

        return new AuthResponse(jwtToken, savedAccount.getUsername(), savedAccount.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // Authentifier l'utilisateur
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Récupérer l'utilisateur
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Générer le token JWT
        String jwtToken = jwtService.generateToken(account);

        return new AuthResponse(jwtToken, account.getUsername(), account.getEmail());
    }
}
