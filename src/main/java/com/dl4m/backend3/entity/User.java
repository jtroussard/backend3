package com.dl4m.backend3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// TODO centralize the reg rules

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    public User(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    @Column(nullable = false, length = 60) // 60 to accommodate hashed passwords
    private String password;

    // TODO figure out the regex later
//    @NotBlank(message = "Email address is required")
    @Column(length = 254, nullable = true) // 60 to accommodate hashed passwords
    private String email;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

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
