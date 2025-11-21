package com.javaapirestgosse.config;

import com.javaapirestgosse.model.*;
import com.javaapirestgosse.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
        AccountRepository accountRepository,
        RoleRepository roleRepository,
        ProductRepository productRepository,
        OrdersRepository ordersRepository,
        NoticeRepository noticeRepository,
        PasswordEncoder passwordEncoder) {
        
        return args -> {
            // 1. Création des Rôles
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER", null)));

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN", null)));

            // 2. Création des Utilisateurs
            // Admin
            if (!accountRepository.existsByUsername("admin")) {
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(new HashSet<>(Set.of(roleUser, roleAdmin)));
                accountRepository.save(admin);
            }

            // Jean Dupont (Utilisateur avec commandes passées)
            Account jean = accountRepository.findByUsername("jean.dupont")
                    .orElseGet(() -> {
                        Account acc = new Account();
                        acc.setUsername("jean.dupont");
                        acc.setEmail("jean.dupont@example.com");
                        acc.setPassword(passwordEncoder.encode("motdepasse123"));
                        acc.setRoles(new HashSet<>(Set.of(roleUser)));
                        
                        Address addr = new Address();
                        addr.setStreet("10 Rue de la Paix");
                        addr.setCity("Paris");
                        addr.setPostalCode("75002");
                        addr.setCountry("France");
                        acc.setAddress(addr);
                        
                        return accountRepository.save(acc);
                    });

            // Marie Martin (Utilisateur avec panier en cours)
            Account marie = accountRepository.findByUsername("marie.martin")
                    .orElseGet(() -> {
                        Account acc = new Account();
                        acc.setUsername("marie.martin");
                        acc.setEmail("marie.martin@example.com");
                        acc.setPassword(passwordEncoder.encode("motdepasse123"));
                        acc.setRoles(new HashSet<>(Set.of(roleUser)));
                        
                        Address addr = new Address();
                        addr.setStreet("25 Avenue des Champs-Élysées");
                        addr.setCity("Paris");
                        addr.setPostalCode("75008");
                        addr.setCountry("France");
                        acc.setAddress(addr);
                        
                        return accountRepository.save(acc);
                    });

            // Paul Durand (Nouvel utilisateur)
            Account paul = accountRepository.findByUsername("paul.durand")
                    .orElseGet(() -> {
                        Account acc = new Account();
                        acc.setUsername("paul.durand");
                        acc.setEmail("paul.durand@example.com");
                        acc.setPassword(passwordEncoder.encode("motdepasse123"));
                        acc.setRoles(new HashSet<>(Set.of(roleUser)));
                        
                        Address addr = new Address();
                        addr.setStreet("5 Boulevard des Capucines");
                        addr.setCity("Lyon");
                        addr.setPostalCode("69001");
                        addr.setCountry("France");
                        acc.setAddress(addr);
                        
                        return accountRepository.save(acc);
                    });

            // 3. Création du Catalogue Produits
            if (productRepository.count() == 0) {
                // Tech
                createProduct(productRepository, "Smartphone X", "Dernier modèle, 128Go", new BigDecimal("899.99"), 50);
                createProduct(productRepository, "Écouteurs Sans Fil", "Réduction de bruit active", new BigDecimal("129.90"), 100);
                createProduct(productRepository, "Montre Connectée", "Suivi santé et sport", new BigDecimal("159.99"), 30);
                createProduct(productRepository, "Clavier Mécanique", "Switches rouges, rétroéclairé RGB", new BigDecimal("89.90"), 40);
                createProduct(productRepository, "Écran 27 pouces", "4K UHD, IPS", new BigDecimal("299.00"), 5);
                createProduct(productRepository, "Console de Jeu", "Nouvelle génération", new BigDecimal("499.99"), 0);

                // Maison & Cuisine
                createProduct(productRepository, "Machine à Café", "Espresso automatique", new BigDecimal("349.50"), 15);
                createProduct(productRepository, "Mixeur Plongeant", "Inox, 800W", new BigDecimal("49.90"), 60);
                createProduct(productRepository, "Set de Couteaux", "5 pièces, acier japonais", new BigDecimal("89.00"), 25);
                createProduct(productRepository, "Lampe de Bureau", "LED, intensité réglable", new BigDecimal("35.00"), 80);

                // Sport & Loisirs
                createProduct(productRepository, "Tapis de Yoga", "Antidérapant, écologique", new BigDecimal("29.90"), 100);
                createProduct(productRepository, "Haltères 5kg", "Set de 2, néoprène", new BigDecimal("24.90"), 40);
                createProduct(productRepository, "Sac à Dos", "Imperméable, pour ordinateur 15 pouces", new BigDecimal("45.00"), 50);
                createProduct(productRepository, "Livre: Apprendre Java", "Le guide complet pour débutants", new BigDecimal("29.99"), 200);
                createProduct(productRepository, "Jeu de Société", "Stratégie et coopération", new BigDecimal("39.90"), 15);
            }

            // 4. Création des Commandes et Avis (si vide)
            if (ordersRepository.count() == 0) {
                List<Product> products = productRepository.findAll();
                Product smartphone = findProduct(products, "Smartphone X");
                Product book = findProduct(products, "Livre: Apprendre Java");
                Product coffeeMachine = findProduct(products, "Machine à Café");
                Product yogaMat = findProduct(products, "Tapis de Yoga");
                Product mixer = findProduct(products, "Mixeur Plongeant");

                // Commande 1 : Jean (LIVRÉE)
                if (smartphone != null && book != null) {
                    Orders order1 = createOrder(ordersRepository, jean, "DELIVERED", LocalDateTime.now().minusDays(10),
                            new OrderItem(smartphone, 1), new OrderItem(book, 2));
                    
                    createNotice(noticeRepository, jean, order1, smartphone, 5, "Excellent téléphone, très fluide !");
                    createNotice(noticeRepository, jean, order1, book, 4, "Très bon livre, mais un peu dense.");
                }

                // Commande 2 : Marie (EN COURS)
                if (coffeeMachine != null) {
                    createOrder(ordersRepository, marie, "PROCESSING", LocalDateTime.now().minusHours(2),
                            new OrderItem(coffeeMachine, 1));
                }

                // Commande 3 : Paul (LIVRÉE) - Sport
                if (yogaMat != null) {
                    Orders order3 = createOrder(ordersRepository, paul, "DELIVERED", LocalDateTime.now().minusDays(20),
                            new OrderItem(yogaMat, 1));
                    
                    createNotice(noticeRepository, paul, order3, yogaMat, 5, "Parfait pour mes séances matinales.");
                }

                // Commande 4 : Paul (ANNULÉE) - Cuisine
                if (mixer != null) {
                    createOrder(ordersRepository, paul, "CANCELLED", LocalDateTime.now().minusDays(2),
                            new OrderItem(mixer, 1));
                }
            }

            System.out.println("========================================");
            System.out.println("   Données de démonstration (FR) chargées !");
            System.out.println("   Comptes :");
            System.out.println("     - admin / admin123");
            System.out.println("     - jean.dupont / motdepasse123");
            System.out.println("     - marie.martin / motdepasse123");
            System.out.println("     - paul.durand / motdepasse123");
            System.out.println("========================================");
        };
    }

    private void createProduct(ProductRepository repo, String name, String desc, BigDecimal price, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setStockQuantity(stock);
        p.setAvailableQuantity(stock);
        repo.save(p);
    }

    private Product findProduct(List<Product> products, String name) {
        return products.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    private Orders createOrder(OrdersRepository repo, Account account, String status, LocalDateTime date, OrderItem... items) {
        Orders order = new Orders();
        order.setAccount(account);
        order.setOrderDate(date);
        order.setStatus(status);
        
        List<OrderDetails> details = new java.util.ArrayList<>();
        for (OrderItem item : items) {
            OrderDetails d = new OrderDetails();
            d.setOrders(order);
            d.setProduct(item.product);
            d.setQuantity(item.quantity);
            d.setUnitPrice(item.product.getPrice());
            details.add(d);
        }
        order.setOrderDetails(details);
        return repo.save(order);
    }

    private void createNotice(NoticeRepository repo, Account account, Orders order, Product product, int rating, String comment) {
        Notice notice = new Notice();
        notice.setAccount(account);
        notice.setOrders(order);
        notice.setProduct(product);
        notice.setRating(rating);
        notice.setComment(comment);
        notice.setCreatedAt(LocalDateTime.now());
        repo.save(notice);
    }

    private record OrderItem(Product product, int quantity) {}
}
