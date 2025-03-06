# DEVELOPMENT ROADMAP

```curl
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "registerationTestUser",
    "email": "registerationTestUser@example.com",
    "password": "test1!Password",
    "confirmPassword": "test1!Password"
}'
```
---

## ✅ **Phase 1: Project Setup**
### [X] **1. Initialize Spring Boot Project**
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
### [X] **2. Basic Authentication Setup**
- Add `User` entity and `UserRepo`
- Create `UserService` for retrieving users
- Configure `SecurityConfig` to allow **all requests (temporarily)**
- Add **test user** in Postgresql DB for testing
- ✅ **Functional Commit**

### [X] **3. Implement JWT Token Authentication**
- Create `JwtUtils` for token generation and validation
- Implement `JwtAuthFilter` to validate JWTs <==== PROJECT IS HERE
- Modify `SecurityConfig`:
    - Enforce JWT authentication
    - Restrict secured endpoints
- Create `/login` endpoint to issue JWTs
- ✅ **Functional Commit**

### [X] **4. Secure Token Storage (HttpOnly Cookies)**
- Modify `JwtUtils` to store JWT in **HttpOnly, Secure** cookies
- Update `/login` endpoint to send token as a **cookie response**
- Ensure frontend **no longer accesses JWT directly**
- Modify `SecurityConfig` to:
    - Accept tokens from cookies instead of `Authorization` headers
- ✅ **Functional Commit**

### [X] **5. Role-Based Access Control (RBAC)**
- Expand `User` entity with **roles** (Admin, User, etc.)
- Modify `SecurityConfig`:
    - Restrict endpoints based on user roles
    - Enforce `@PreAuthorize` where needed
- Update `JwtUtils` to include roles in JWT
- Ensure `JwtAuthFilter` extracts roles from token
- ✅ **Functional Commit**

### [X] **5 and 1/2. Role-Based Access Control (RBAC)**
- Expand `User` entity with **roles** (Admin, User, etc.)
- Accept login request parameters in body and get front end to hash the password before sending
- Redo hand validations
- ✅ **Functional Commit**

--- 

## ✅ **Phase 4: Frontend Integration**
### [ ] **9. Modify Frontend to Use Spring Boot API**
- Update `axios.js` to point to the new backend
- Update `/login` and `/register` API calls
- Ensure JWT is sent in **cookies, not headers**
- Update `RequireAuth.jsx` to handle role-based routing properly
- - Discuss and consider building a controller endpoint to fetch the user roles for frontend auth context
- ```javascript
  useEffect(() => {
  const fetchUserData = async () => {
  try {
  const response = await axios.get('/auth/me', { withCredentials: true });
  const roles = response?.data?.roles || [];
  setAuth({ user, roles });
  } catch (err) {
  console.error('Failed to fetch user data:', err);
  }
  };

  if (accessToken) {
  fetchUserData();
  }
  }, [accessToken]);
  ```
- ✅ **Functional Commit**

### [ ] **10. End-to-End Testing**
- Test authentication flow with React frontend
- Verify role-based access control
- Validate refresh token handling
- ✅ **Functional Commit**

### [ ] **11. Prepare Application for Cloud Deployment**
- Create Task list for this intent, don't forget we need to deploy the data base to the cloud as well
- Create a kill switch once the applicaiton bill reaches x dollars
- Create a db backup schedule
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

## Security User Stories that need to be covered in the secuirty chain

### **JWT Security Filter Scenarios**

Below is a **comprehensive list of possible request scenarios** and their expected outcomes when processed by the **JwtAuthFilter** and **SecurityFilterChain**.

---

### **Authenticated Users**
#### **Scenario 1**
**Given:** A user is authenticated  
**When:** They make a request to a protected resource with a **valid token**  
**Then:**
- `JwtAuthFilter` extracts and validates the token ✅
- `JwtAuthFilter` loads `UserDetails` and sets `SecurityContextHolder` ✅
- **Request is passed down the filter chain** 🔽
- **SecurityFilterChain** checks authorization and allows access ✅

---

#### **Scenario 2**
**Given:** A user is authenticated  
**When:** They make a request to an unprotected resource  
**Then:**
- `JwtAuthFilter` runs but **does not block the request** ✅
- **Request is passed down the filter chain** 🔽
- **SecurityFilterChain** permits access ✅

---

#### **Scenario 3**
**Given:** A user is authenticated  
**When:** They make a request with a valid token but **their role does not have permission**  
**Then:**
- `JwtAuthFilter` extracts and validates the token ✅
- `JwtAuthFilter` loads `UserDetails` and sets `SecurityContextHolder` ✅
- **SecurityFilterChain rejects the request** ❌

---

### **Unauthenticated Users**
#### **Scenario 4**
**Given:** A user is unauthenticated  
**When:** They make a request to a **protected resource without a token**  
**Then:**
- `JwtAuthFilter` does not find a token ❌
- **Request is passed down the filter chain** 🔽
- **SecurityFilterChain rejects the request** (`403 Forbidden`) ❌

---

#### **Scenario 5**
**Given:** A user is unauthenticated  
**When:** They make a request to an **unprotected resource**  
**Then:**
- `JwtAuthFilter` does not find a token ❌
- **Request is passed down the filter chain** 🔽
- **SecurityFilterChain permits access** ✅

---

#### **Scenario 6**
**Given:** A user is unauthenticated  
**When:** They make a request to **log in** with valid credentials  
**Then:**
- `JwtAuthFilter` does not interfere ❌
- **Authentication process proceeds** 🔽
- Security grants access and issues JWT ✅

---

### **Expired Tokens**
#### **Scenario 7**
**Given:** A user was authenticated but their **token has expired**  
**When:** They make a request to a protected resource  
**Then:**
- `JwtAuthFilter` **extracts the token** ✅
- `JwtAuthFilter` **detects expiration** ❌
- **Request is stopped** with `401 Unauthorized` ❌

---

#### **Scenario 8**
**Given:** A user was authenticated but their **token has expired**  
**When:** They make a request to refresh their token (`/refresh-token`)  
**Then:**
- `JwtAuthFilter` does not interfere ❌
- **Refresh token mechanism handles request** ✅
- **New token is issued** ✅

---

### **Token Tampering & Injection**
#### **Scenario 9**
**Given:** A user manually modifies their **valid token** (tampered signature)  
**When:** They attempt to access a protected resource  
**Then:**
- `JwtAuthFilter` **detects signature mismatch** ❌
- **Request is blocked with `401 Unauthorized`** ❌

---

#### **Scenario 10**
**Given:** A user **loads a JWT token from another website** into their browser cookies  
**When:** They make a request to a protected resource  
**Then:**
- `JwtAuthFilter` **validates the token** ✅
- If token is valid for an existing user, **request is processed** ✅
- **(If domain-level cookie restrictions were not enforced, this could be a security risk!)** ⚠️

---

#### **Scenario 11**
**Given:** A user tries to use **a stolen token from another user**  
**When:** They make a request to a protected resource  
**Then:**
- `JwtAuthFilter` **validates the token** ✅
- **SecurityFilterChain allows access if the token is still valid** ⚠️
- **This could be prevented with refresh tokens or IP validation**

---

### **CSRF-Related Scenarios**
#### **Scenario 12**
**Given:** A user is logged in and has a valid JWT  
**When:** They are targeted by a **CSRF attack** with a forged request  
**Then:**
- **JwtAuthFilter processes the token as usual** ✅
- **CSRF protection (if enabled) prevents the attack** ✅

---

#### **Scenario 13**
**Given:** A user **logs out**  
**When:** They try to access a protected resource  
**Then:**
- `JwtAuthFilter` does not find a valid token ❌
- **Request is blocked** with `403 Forbidden` ❌

---

### **Token Revocation & Blacklisting**
#### **Scenario 14**
**Given:** A user logs out and their token is **explicitly revoked**  
**When:** They try to use the old token  
**Then:**
- `JwtAuthFilter` still sees the token ✅
- But **token blacklist validation rejects it** ❌

---

#### **Scenario 15**
**Given:** A user has multiple sessions and **one of their sessions is revoked**  
**When:** They try to use the old token  
**Then:**
- `JwtAuthFilter` **still validates the token** ✅
- But **token blacklist validation rejects it** ❌

---

### **Replay Attack Prevention**
#### **Scenario 16**
**Given:** An attacker **captures a JWT token** from a legitimate request  
**When:** They try to replay the request later  
**Then:**
- **JwtAuthFilter validates the token** ✅
- **If replay prevention (e.g., jti claim) is implemented, request is blocked** ❌

---

### **Rate-Limiting & Abuse Prevention**
#### **Scenario 17**
**Given:** A user makes **too many failed login attempts**  
**When:** They try to log in again  
**Then:**
- **Brute force protection (if enabled) locks account temporarily** ❌

---

#### **Scenario 18**
**Given:** A user is logged in  
**When:** They make **too many API requests in a short time**  
**Then:**
- **Rate-limiting middleware (if enabled) blocks excessive requests** ❌

---

### **Unusual Token Behavior**
#### **Scenario 19**
**Given:** A user has a **valid JWT**  
**When:** They change their password  
**Then:**
- **JwtAuthFilter does not invalidate existing tokens by default** ⚠️
- **Best practice: Token should be invalidated after password reset**

---

#### **Scenario 20**
**Given:** A user has a **valid JWT**  
**When:** Their account is deleted  
**Then:**
- **Token remains technically valid** unless explicitly revoked ❌

---

### **Invalid Token Edge Cases**
#### **Scenario 21**
**Given:** A user provides an **empty token**  
**When:** They make a request  
**Then:**
- `JwtAuthFilter` **detects the empty token** ❌
- **Request is rejected with `401 Unauthorized`** ❌

---

#### **Scenario 22**
**Given:** A user provides an **incorrectly formatted JWT**  
**When:** They make a request  
**Then:**
- `JwtAuthFilter` **detects malformed token** ❌
- **Request is rejected with `401 Unauthorized`** ❌

---

### **JWT Expiry & Refresh Timing**
#### **Scenario 23**
**Given:** A user’s token is **expiring soon**  
**When:** They make a request  
**Then:**
- `JwtAuthFilter` still allows request ✅
- **Frontend should trigger token refresh** before expiry

---

#### **Scenario 24**
**Given:** A user’s token **has just expired**  
**When:** They make a request  
**Then:**
- `JwtAuthFilter` detects expiration ❌
- **Request is blocked with `401 Unauthorized`** ❌

---

#### **Scenario 25**
**Given:** A user’s refresh token **has expired**  
**When:** They request a new access token  
**Then:**
- **Refresh process fails, requiring full reauthentication** ❌

---

### **More Scenarios**
#### **Scenario 26-30**
26. **Token is issued but missing required claims** -> **Rejected** ❌
27. **User logs in twice with different devices** -> **Both sessions valid** ✅
28. **Token with future `iat` (issued at) timestamp** -> **Rejected** ❌
29. **Token issued but manipulated after signing** -> **Rejected** ❌
30. **User account is disabled but token is still valid** -> **Potential access unless checked** ⚠️

---

## Long Term TODOs
1. Set up debugger in IDE
2. Use Debugger to walk through security chain, confirm authentication provider behavior.
3. centralize regex patterns for passwords and username constraints etc...
4. Generic third party sign in
5. Generic plugin for authenticators
6. Add Captcha as well or cloudflare DDOS tool?
