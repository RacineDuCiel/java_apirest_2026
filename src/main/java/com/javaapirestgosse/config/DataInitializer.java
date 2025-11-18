package com.javaapirestgosse.config;

import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.Role;
import com.javaapirestgosse.repository.AccountRepository;
import com.javaapirestgosse.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            AccountRepository accountRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Créer les rôles s'ils n'existent pas
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_USER");
                        return roleRepository.save(newRole);
                    });

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_ADMIN");
                        return roleRepository.save(newRole);
                    });

            // Créer un compte admin par défaut s'il n'existe pas
            if (!accountRepository.existsByUsername("admin")) {
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleUser);
                adminRoles.add(roleAdmin);
                admin.setRoles(adminRoles);
                
                accountRepository.save(admin);
                
                System.out.println("========================================");
                System.out.println("✅ Admin par défaut créé avec succès !");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
                System.out.println("   Roles: ROLE_USER, ROLE_ADMIN");
                System.out.println("========================================");
            } else {
                System.out.println("ℹ️  Admin déjà existant, pas de création.");
            }
        };
    }
}
