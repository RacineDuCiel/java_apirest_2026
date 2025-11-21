# JAVA_API_REST_GOSSE

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Ce projet implémente une API REST pour une application de commerce, développée avec Java 21 et Spring Boot 3.5.6.

## Table des Matières

1. [Description du Projet](#description-du-projet)
2. [Architecture Technique](#architecture-technique)
3. [Modèle de Données](#modèle-de-données)
4. [Fonctionnalités](#fonctionnalités)
5. [Déploiement avec Docker](#déploiement-avec-docker)
6. [Installation Locale](#installation-locale)
7. [Documentation et Tests](#documentation-et-tests)
8. [Configuration](#configuration)
9. [Auteurs](#auteurs)

---

## Description du Projet

Cette application backend gère les fonctionnalités essentielles d'une plateforme de vente en ligne. Elle permet la gestion des utilisateurs, du catalogue de produits, des commandes et des avis clients.

**Caractéristiques principales :**
*   **Authentification** : Utilisation de JWT (JSON Web Tokens) pour l'authentification stateless.
*   **Base de données** : Utilisation de H2 (in-memory) pour le développement et les tests.
*   **Validation** : Vérification des données entrantes et validation des adresses via l'API `data.geopf.fr`.
*   **Gestion des erreurs** : Centralisation des exceptions via `@ControllerAdvice`.

## Architecture Technique

Le projet est structuré selon une architecture en couches standard.

```text
src/main/java/com/javaapirestgosse
├── config       # Classes de configuration (Sécurité, Swagger, CORS)
├── controller   # Contrôleurs REST (Points d'entrée)
├── dto          # Objets de transfert de données (DTO)
├── exception    # Gestionnaire global d'exceptions
├── model        # Entités JPA
├── repository   # Interfaces d'accès aux données (Spring Data JPA)
├── security     # Configuration de la sécurité et filtres JWT
└── service      # Couche métier
```

### Détails d'implémentation
*   **DTO (Data Transfer Objects)** : Les entités JPA ne sont pas exposées directement via l'API. Des objets DTO sont utilisés pour transférer les données entre le client et le serveur.
*   **Gestion des exceptions** : Les erreurs sont capturées et renvoyées au client sous un format JSON standardisé.

## Spécificités Techniques

### Sécurité Granulaire (Method Security)
Au-delà de l'authentification JWT, l'application implémente une sécurité fine au niveau des méthodes via Spring Security (`@PreAuthorize`).
Une classe personnalisée `AccountSecurity` permet de définir des règles d'accès contextuelles.

*Exemple :* Seul l'administrateur ou le propriétaire d'un compte peut modifier ses informations.
```java
@PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
public ResponseEntity<AccountResponse> updateAccount(...)
```

### Gestion Standardisée des Erreurs
Toutes les exceptions (métier, validation, accès) sont interceptées par un `GlobalExceptionHandler`. L'API retourne systématiquement une réponse structurée pour faciliter le débogage côté client.

**Format de réponse d'erreur :**
```json
{
  "timestamp": "2023-11-21T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Le champ 'email' doit être une adresse email valide",
  "path": "/api/auth/register"
}
```

### Validation des Données
L'intégrité des données est garantie par l'utilisation de l'API `Jakarta Validation` directement dans les DTOs.
*   **Email** : Vérification du format.
*   **Champs obligatoires** : `@NotBlank`, `@NotNull`.
*   **Logique métier** : Validation personnalisée si nécessaire.



## Fonctionnalités

### Authentification et Rôles
*   **Inscription/Connexion** : Obtention d'un token JWT.
*   **Contrôle d'accès (RBAC)** :
    *   `PUBLIC` : Accès au catalogue et à l'authentification.
    *   `USER` : Accès à la création de commandes et au profil utilisateur.
    *   `ADMIN` : Accès à la gestion des utilisateurs, des stocks et aux métriques.

### Gestion E-commerce
*   **Catalogue** : Consultation des produits et état des stocks.
*   **Commandes** : Processus de commande avec vérification des stocks.
*   **Avis** : Ajout de notes et commentaires sur les produits commandés.

### Intégrations
*   **Géocodage** : Validation de l'existence des adresses de livraison via une API externe.

## Déploiement avec Docker

Le projet inclut une configuration Docker pour faciliter le déploiement.

1.  **Cloner le dépôt**
    ```bash
    git clone <URL_DU_REPO>
    cd java_apirest_2026
    ```

2.  **Lancer l'application**
    ```bash
    docker compose up --build
    ```

3.  **Accès**
    *   API : `http://localhost:8080`
    *   Documentation Swagger : `http://localhost:8080/docs`
    *   Console H2 : `http://localhost:8080/h2-console`

## Installation Locale

Instructions pour une exécution sans Docker.

### Prérequis
*   Java 21 JDK
*   Maven 3.9+

### Exécution
```bash
# Compilation et installation des dépendances
./mvnw clean install

# Démarrage de l'application
./mvnw spring-boot:run
```

## Environnement de Développement & Tests

Pour faciliter le test et la validation des fonctionnalités, l'application est pré-configurée avec des outils et des données spécifiques.

> [NOTE]
> Ces configurations sont destinées uniquement à un environnement de développement ou de démonstration. En production, la base de données H2 doit être remplacée par une base persistante (PostgreSQL, MySQL) et le chargement automatique des données désactivé.

### Données Initiales (Seeder)
Au démarrage, la classe `DataInitializer` peuple automatiquement la base de données avec un jeu de données complet pour simuler une activité réelle :
*   **Catalogue** : Une quinzaine de produits (High-Tech, Maison, Sport).
*   **Utilisateurs** : Plusieurs comptes avec des historiques de commandes variés.
*   **Commandes & Avis** : Des commandes livrées, en cours ou annulées, ainsi que des avis clients.

### Comptes de Démonstration
Voici les identifiants pré-créés pour tester les différents rôles :

| Rôle | Nom d'utilisateur | Mot de passe | Description |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Accès complet au back-office. |
| **USER** | `jean.dupont` | `motdepasse123` | Client avec historique de commandes. |
| **USER** | `marie.martin` | `motdepasse123` | Client avec panier en cours. |
| **USER** | `paul.durand` | `motdepasse123` | Client avec commandes annulées. |

### Console H2
Une interface web est disponible pour explorer et requêter la base de données en mémoire directement.

*   **URL** : `http://localhost:8080/h2-console`
*   **JDBC URL** : `jdbc:h2:mem:testdb`
*   **User** : `sa`
*   **Password** : `password`

## Documentation et Tests

### Swagger UI
La documentation OpenAPI est générée automatiquement et accessible à l'adresse suivante :
**[http://localhost:8080/docs](http://localhost:8080/docs)**

### Tests API (Bruno)
Une collection complète de tests est disponible dans le dossier `bruno-tests` pour valider l'intégralité de l'API.

**Modules testés :**
1.  **01-Auth** : Inscription et Connexion.
2.  **02-Accounts** : CRUD complet, gestion des droits (Admin vs User) et sécurité.
3.  **03-Products** : Consultation du catalogue.
4.  **04-Orders** : Création de commandes (validation stocks, panier vide) et historique.
5.  **05-Notices** : Ajout d'avis sur les produits achetés.

**Configuration requise :**
Pour que les variables (tokens, IDs) soient conservées entre les requêtes, vous devez sélectionner l'environnement **Local** dans Bruno :
1.  Ouvrez Bruno.
2.  En haut à droite, cliquez sur le menu déroulant des environnements (souvent "No Environment").
3.  Sélectionnez **Local**.

**Exécution :**
Il est recommandé de lancer la collection entière ou les dossiers dans l'ordre numérique, car certains tests dépendent des données (tokens, IDs) générées par les précédents.

## Configuration

Les paramètres de l'application sont définis dans `src/main/resources/application.properties`.

| Propriété | Description | Valeur par défaut |
| :--- | :--- | :--- |
| `server.port` | Port d'écoute HTTP | `8080` |
| `JWT_SECRET` | Clé secrète pour la signature JWT | *(Valeur de développement)* |
| `spring.profiles.active` | Profil Spring actif | `default` |
 
---
*Théo GOSSE - I5 FISA RIOC - JAVA J2EE*
