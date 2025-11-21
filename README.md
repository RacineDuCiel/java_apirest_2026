# JAVA_API_REST_GOSSE

Bienvenue sur le projet **JAVA_API_REST_GOSSE**. Ce projet est une API RESTful développée avec **Spring Boot 3.5.6** et **Java 21**. Elle gère un système e-commerce complet incluant l'authentification, la gestion des comptes utilisateurs, le catalogue produits, les commandes et les avis clients.

## Table des Matières

1. [Aperçu du Projet](#aperçu-du-projet)
2. [Technologies Utilisées](#technologies-utilisées)
3. [Fonctionnalités](#fonctionnalités)
4. [Architecture Technique](#architecture-technique)
5. [Prérequis](#prérequis)
6. [Installation et Démarrage](#installation-et-démarrage)
7. [Configuration](#configuration)
8. [Documentation de l'API](#documentation-de-lapi)
9. [Sécurité](#sécurité)
10. [Gestion des Erreurs](#gestion-des-erreurs)

---

## Aperçu du Projet

Cette application backend fournit une infrastructure robuste pour une plateforme e-commerce. Elle a été conçue pour être modulaire, sécurisée et facilement extensible. L'API permet aux clients de s'inscrire, de consulter des produits, de passer des commandes et de laisser des avis, tout en offrant aux administrateurs des outils de gestion complets.

## Technologies Utilisées

*   **Langage** : Java 21
*   **Framework** : Spring Boot 3.5.6
*   **Sécurité** : Spring Security, JWT (JSON Web Tokens)
*   **Base de Données** : H2 Database (In-Memory pour le développement)
*   **ORM** : Spring Data JPA (Hibernate)
*   **Validation** : Hibernate Validator
*   **Documentation** : SpringDoc OpenAPI (Swagger UI)
*   **Outils de Build** : Maven
*   **Tests API** : Bruno

## Fonctionnalités

### Authentification et Utilisateurs
*   **Inscription et Connexion** : Sécurisées via JWT.
*   **Gestion des Rôles** : Distinction stricte entre `ROLE_USER` et `ROLE_ADMIN`.
*   **Gestion de Profil** : Les utilisateurs peuvent modifier leurs informations personnelles.

### E-commerce
*   **Catalogue Produits** : Consultation des produits avec gestion des stocks en temps réel.
*   **Système de Commandes** : Création de commandes avec décrémentation automatique des stocks.
*   **Historique** : Les utilisateurs peuvent consulter leur historique de commandes avec pagination et tri.
*   **Avis Clients** : Possibilité de noter et commenter les produits achetés.

### Services Externes
*   **Validation d'Adresse** : Intégration de l'API gouvernementale `data.geopf.fr` pour valider les adresses de livraison en France.

## Architecture Technique

Le projet respecte une architecture en couches standard pour assurer la maintenabilité et la séparation des responsabilités.

```text
src/main/java/com/javaapirestgosse
├── config       # Configuration (Sécurité, Swagger, etc.)
├── controller   # Points d'entrée de l'API (REST Controllers)
├── dto          # Objets de transfert de données (Request/Response)
├── exception    # Gestion centralisée des exceptions
├── model        # Entités JPA (Base de données)
├── repository   # Interfaces d'accès aux données (DAO)
├── security     # Logique de sécurité spécifique (ex: vérification de propriété)
└── service      # Logique métier
```

## Prérequis

*   **Java JDK 21**
*   **Maven 3.9+**
*   **Git**

## Installation et Démarrage

### 1. Récupération du code source

```bash
git clone <URL_DU_DEPOT>
cd java_apirest_2026
```

### 2. Compilation

```bash
# Sous Linux / macOS
./mvnw clean install

# Sous Windows
mvnw.cmd clean install
```

### 3. Exécution

```bash
# Sous Linux / macOS
./mvnw spring-boot:run

# Sous Windows
mvnw.cmd spring-boot:run
```

Le serveur démarrera par défaut sur le port **8080**.

## Configuration

La configuration de l'application se trouve dans `src/main/resources/application.properties`.

### Paramètres Principaux

| Clé | Description | Valeur par défaut |
| :--- | :--- | :--- |
| `server.port` | Port d'écoute du serveur | `8080` |
| `spring.datasource.url` | URL de connexion JDBC | `jdbc:h2:mem:testdb` |
| `jwt.secret` | Clé de signature des tokens | *(Définie dans le code)* |
| `jwt.expiration` | Durée de validité du token | `86400000` (24h) |

### Variables d'Environnement

Pour la production, il est recommandé de surcharger les propriétés sensibles via des variables d'environnement :

*   `JWT_SECRET` : Pour définir une clé secrète robuste.

## Documentation de l'API

L'API est entièrement documentée via OpenAPI (Swagger). Une interface interactive est disponible lorsque l'application est lancée.

*   **URL Swagger UI** : `http://localhost:8080/docs`
*   **Spécification JSON** : `http://localhost:8080/v3/api-docs`

## Sécurité

L'authentification est gérée via des tokens **Bearer JWT**.

1.  L'utilisateur s'authentifie via `/api/auth/login`.
2.  L'API retourne un token JWT.
3.  Ce token doit être inclus dans l'en-tête `Authorization` de chaque requête protégée :

```http
Authorization: Bearer <VOTRE_TOKEN>
```

### Contrôle d'Accès

*   **Public** : Inscription, Connexion, Consultation des produits.
*   **Authentifié** : Création de commande, Avis, Consultation de profil.
*   **Admin** : Gestion complète des comptes, Accès aux métriques (Actuator).

## Gestion des Erreurs

L'API utilise un format de réponse d'erreur standardisé pour faciliter le débogage côté client.

**Exemple de réponse d'erreur (400 Bad Request) :**

```json
{
  "timestamp": "2023-11-20T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Le champ 'email' doit être une adresse email valide",
  "path": "/api/auth/register"
}
```
