# Title

## Development

### Useful Commands

| command | description |
|---------|-------------|
|`./mvnw clean install -Dspring.profiles.active=local`| clean install with active profile|
|`./mvnw spring-boot:run -Dspring-boot.run.profiles=local`| run the apring-boot app locally with the local profile active|

## Roadmap

### Features to implement

### [ ] **Implement Refresh Tokens**
- Create `RefreshTokenService`
- Modify `JwtUtils` to:
    - Issue short-lived access tokens
    - Generate long-lived refresh tokens
- Implement `/refresh-token` endpoint
- Store refresh token in HttpOnly cookies
- Adjust `JwtAuthFilter` to handle expired access tokens

### [ ] **Re-enable CSRF Protection & CORS**
- Enable CSRF protection for non-API requests
- Configure CORS policy:
    - Allow requests only from the frontend domain
    - Restrict methods and headers properly

### [ ] **Advanced Security Features**
- **DONE** Implement password hashing with `BCrypt`
- Add account lockout for repeated login failures
- Implement JWT key rotation
- Implement 2FA 

