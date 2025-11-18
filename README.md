# Java API Rest Gosse

API Spring Boot permettant d'enregistrer des utilisateurs, gérer leurs comptes et passer des commandes sur des produits, avec sécurisation JWT et validation d'adresse externe.

## 🔐 Rôles & politiques d'accès

| HTTP | Route | Description | Rôles autorisés |
|------|-------|-------------|-----------------|
| POST | `/api/auth/register` | Création d'un utilisateur + émission d'un token | Public |
| POST | `/api/auth/login` | Authentification et récupération d'un token | Public |
| GET | `/api/accounts` | Liste tous les comptes | `ADMIN` |
| GET | `/api/accounts/{id}` | Récupère un compte par identifiant | `ADMIN` |
| POST | `/api/accounts` | Crée un compte (ex: adresse ou notice associée) | `USER`, `ADMIN` |
| PUT | `/api/accounts/{id}` | Met à jour un compte existant | `ADMIN` |
| DELETE | `/api/accounts/{id}` | Supprime un compte | `ADMIN` |
| GET | `/api/orders/me` | Liste paginée des commandes de l'utilisateur, filtrable par statut | `USER`, `ADMIN` |
| POST | `/api/orders` | Crée une commande et décrémente les stocks produits | `USER`, `ADMIN` |
| GET | `/api/products` | Liste le catalogue avec disponibilités et alerte `lowStock` | `USER`, `ADMIN` |

Les contrôleurs exposent ces règles via `@PreAuthorize` et la configuration de sécurité complète les restrictions réseau.

## 📘 Documentation API

- Swagger UI : `http://localhost:8080/docs`
- OpenAPI JSON : `http://localhost:8080/api-docs`

## 🧪 Tester l'application

```bash
./mvnw test
```

## ▶️ Lancer l'API localement

```bash
./mvnw spring-boot:run
```

Une fois démarrée, utilisez les scripts de la collection Bruno (`bruno-tests/`) ou n'importe quel client HTTP pour appeler l'API avec les rôles adaptés.

## 🆕 Nouveautés clés

- **Historique commandes** : `GET /api/orders/me?status=CREATED&page=0&size=5&sort=orderDate&direction=DESC` retourne un résumé paginé avec le nombre d'articles et le montant total.
- **Création de commande** : `POST /api/orders` accepte une liste d'items `{ "productId": 1, "quantity": 2 }`, vérifie le stock et met à jour l'inventaire.
- **Alertes stock** : chaque `Product` expose maintenant `availableQuantity` et `lowStock` (seuil configurable dans le code à 5 unités) pour repérer rapidement les produits critiques.
