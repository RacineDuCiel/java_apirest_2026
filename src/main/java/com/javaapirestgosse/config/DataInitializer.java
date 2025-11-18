package com.javaapirestgosse.config;

import com.javaapirestgosse.model.Account;
import com.javaapirestgosse.model.Product;
import com.javaapirestgosse.model.Role;
import com.javaapirestgosse.repository.AccountRepository;
import com.javaapirestgosse.repository.ProductRepository;
import com.javaapirestgosse.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
        AccountRepository accountRepository,
        RoleRepository roleRepository,
        ProductRepository productRepository,
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
                System.out.println("   Admin par défaut créé avec succès !");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
                System.out.println("========================================");
            } else {
                System.out.println("ℹ️  Admin déjà existant, pas de création.");
            }

            if (productRepository.count() == 0) {
                Product espresso = new Product();
                espresso.setName("Café Espresso");
                espresso.setDescription("Torréfaction artisanale, sachet de 250g");
                espresso.setPrice(new BigDecimal("7.90"));
                espresso.setStockQuantity(100);
                espresso.setAvailableQuantity(100);

                Product tea = new Product();
                tea.setName("Thé Matcha");
                tea.setDescription("Matcha premium en poudre 50g");
                tea.setPrice(new BigDecimal("12.50"));
                tea.setStockQuantity(40);
                tea.setAvailableQuantity(40);

                Product mug = new Product();
                mug.setName("Mug thermique");
                mug.setDescription("Mug inox 500ml");
                mug.setPrice(new BigDecimal("19.90"));
                mug.setStockQuantity(25);
                mug.setAvailableQuantity(25);

                productRepository.save(espresso);
                productRepository.save(tea);
                productRepository.save(mug);

                System.out.println("Produits de démonstration ajoutés au catalogue.");
            }
        };
    }
}
