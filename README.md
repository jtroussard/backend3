# Title

## Development

### Useful Commands

| command | description                                                        |
|---------|--------------------------------------------------------------------|
|`./mvnw clean install -Dspring.profiles.active=local`| clean install with active profile                                  |
|`export $(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local` | run the apring-boot app locally with the local profile active|

`./mvnw spring-boot:run -Dspring-boot.run.profiles=local`


## Roadmap

### Production Implementation Research and Considerations

- Private IP with VPC Connector (Serverless VPC Access) VS Cloud SQL Auth Proxy (Sidecar / Built-in with Cloud Run)

### Features to implement

### [ ] **Refactor to be a deployable module**

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

