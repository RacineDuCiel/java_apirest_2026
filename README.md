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
