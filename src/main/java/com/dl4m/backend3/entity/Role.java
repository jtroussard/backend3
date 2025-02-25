package com.dl4m.backend3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    public Role(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role name is required")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank(message = "Role code is required")
    @Column(nullable = false, unique = true, length = 4)
    private int code;

    @Column(length = 50)
    private String displayName;

    // TODO remove before deployment, minor security issue.
    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + displayName + ", code='" + code + "'}";
    }
}
