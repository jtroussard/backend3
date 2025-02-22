package com.dl4m.backend3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// TODO centralize the reg rules

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 24, message = "Username must be between 4 and 24 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9-_]{3,23}$",
            message = "Username must start with a letter and can contain letters, numbers, underscores, and hyphens")
    @Column(nullable = false, unique = true, length = 24)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 24, message = "Password must be between 8 and 24 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$",
            message = "Password must contain at least one uppercase, one lowercase, one number, and one special character (!@#$%)")
    @Column(nullable = false, length = 60) // 60 to accommodate hashed passwords
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // HINT ensure that entities are fetched from the database first before modifying.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{ id= " + id + ", username= " + username + ", " + "createdAt= " + createdAt;
    }

}
