# DEVELOPMENT ROADMAP

This document outlines the step-by-step development plan for the Spring Boot backend integration with the React frontend. Each task represents a **functional commit**, unless otherwise noted.

---

## ✅ **Phase 1: Project Setup**
### [ ] **1. Initialize Spring Boot Project**
- Create a new Spring Boot project with:
    - `spring-boot-starter-web`
    - `spring-boot-starter-security`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-validation`
- Configure `application-local.yml` for **PostgreSQL**
- Implement a basic **REST controller** for testing
- ✅ **Functional Commit**

---

## ✅ **Phase 2: Authentication & Security**
### [ ] **2. Basic Authentication Setup**
- Add `User` entity and `UserRepo`
- Create `UserService` for retrieving users
- Configure `SecurityConfig` to allow **all requests (temporarily)**
- Add **in-memory user** for testing
- ✅ **Functional Commit**

### [ ] **3. Implement JWT Token Authentication**
- Create `JwtUtils` for token generation and validation
- Implement `JwtAuthFilter` to validate JWTs
- Modify `SecurityConfig`:
    - Enforce JWT authentication
    - Restrict secured endpoints
- Create `/login` endpoint to issue JWTs
- ✅ **Functional Commit**

### [ ] **4. Secure Token Storage (HttpOnly Cookies)**
- Modify `JwtUtils` to store JWT in **HttpOnly, Secure** cookies
- Update `/login` endpoint to send token as a **cookie response**
- Ensure frontend **no longer accesses JWT directly**
- Modify `SecurityConfig` to:
    - Accept tokens from cookies instead of `Authorization` headers
- ✅ **Functional Commit**

### [ ] **5. Role-Based Access Control (RBAC)**
- Expand `User` entity with **roles** (Admin, User, etc.)
- Modify `SecurityConfig`:
    - Restrict endpoints based on user roles
    - Enforce `@PreAuthorize` where needed
- Update `JwtUtils` to include roles in JWT
- Ensure `JwtAuthFilter` extracts roles from token
- ✅ **Functional Commit**

### [ ] **6. Implement Refresh Tokens**
- Create `RefreshTokenService`
- Modify `JwtUtils` to:
    - Issue **short-lived access tokens**
    - Generate **long-lived refresh tokens**
- Implement `/refresh-token` endpoint
- Store **refresh token in HttpOnly cookies**
- Adjust `JwtAuthFilter` to handle **expired access tokens**
- ✅ **Functional Commit**

---

## ✅ **Phase 3: Security Enhancements**
### [ ] **7. Re-enable CSRF Protection & CORS**
- Enable **CSRF protection** for non-API requests
- Configure **CORS policy**:
    - Allow requests only from the frontend domain
    - Restrict methods and headers properly
- ✅ **Functional Commit**

### [ ] **8. Advanced Security Features**
- Implement **password hashing** with `BCrypt`
- Add **account lockout** for repeated login failures
- Implement **JWT key rotation**
- (Optional) Implement **2FA**
- ✅ **Functional Commit**

---

## ✅ **Phase 4: Frontend Integration**
### [ ] **9. Modify Frontend to Use Spring Boot API**
- Update `axios.js` to point to the new backend
- Update `/login` and `/register` API calls
- Ensure JWT is sent in **cookies, not headers**
- Update `RequireAuth.jsx` to handle role-based routing properly
- ✅ **Functional Commit**

### [ ] **10. End-to-End Testing**
- Test authentication flow with React frontend
- Verify role-based access control
- Validate refresh token handling
- ✅ **Functional Commit**

---

## **Notes & Mid-Development Adjustments**
- Any necessary changes or refinements can be documented here as we iterate.

---

### **Legend**
✅ **Completed Tasks**  
[ ] **Pending Tasks**  
[Nonfunctional Commit] **Indicates commits that do not yet result in a working backend**

---

This **`DEVELOPMENT.md`** acts as a **PIN comment** to track our progress and ensure a structured, incremental approach.
