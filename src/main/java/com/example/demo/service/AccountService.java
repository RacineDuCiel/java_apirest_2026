package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AddressValidationService addressValidationService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AccountService(AccountRepository accountRepository,
                          AddressValidationService addressValidationService,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.addressValidationService = addressValidationService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé avec l'ID : " + id));
    }

    public Account createAccount(Account account) {
        // Vérifier si l'username existe déjà
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }

        // Vérifier si l'email existe déjà
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // Valider l'adresse si fournie
        if (account.getAddress() != null) {
            boolean isValid = addressValidationService.validateAddress(account.getAddress());
            if (!isValid) {
                throw new IllegalArgumentException("L'adresse fournie n'est pas valide");
            }
        }

        // Hasher le mot de passe
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Attribuer le rôle USER par défaut si aucun rôle n'est défini
        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_USER");
                        return roleRepository.save(newRole);
                    });
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            account.setRoles(roles);
        }

        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé avec l'ID : " + id));

        // Vérifier si le nouveau username est déjà pris par un autre compte
        if (!existingAccount.getUsername().equals(updatedAccount.getUsername()) &&
                accountRepository.existsByUsername(updatedAccount.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }

        // Vérifier si le nouveau email est déjà utilisé par un autre compte
        if (!existingAccount.getEmail().equals(updatedAccount.getEmail()) &&
                accountRepository.existsByEmail(updatedAccount.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // Valider la nouvelle adresse si fournie
        if (updatedAccount.getAddress() != null) {
            boolean isValid = addressValidationService.validateAddress(updatedAccount.getAddress());
            if (!isValid) {
                throw new IllegalArgumentException("L'adresse fournie n'est pas valide");
            }
            existingAccount.setAddress(updatedAccount.getAddress());
        }

        // Mettre à jour les champs
        existingAccount.setUsername(updatedAccount.getUsername());
        existingAccount.setEmail(updatedAccount.getEmail());

        // Hasher le nouveau mot de passe si fourni
        if (updatedAccount.getPassword() != null && !updatedAccount.getPassword().isEmpty()) {
            existingAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
        }

        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Compte non trouvé avec l'ID : " + id);
        }
        accountRepository.deleteById(id);
    }
}
