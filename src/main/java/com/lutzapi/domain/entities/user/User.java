package com.lutzapi.domain.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @NotBlank(message = "User needs to be of type SELLER or BUYER")
    private UserType type;

    @NotNull
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    private String lastName;

    @Column(unique = true)
    @NotNull
    @NotBlank(message = "Document is mandatory")
    private String document;

    @Column(unique = true)
    @NotNull
    @NotBlank(message = "Email is mandatory")
    private String email;

    @JsonIgnore
    @NotNull
    @NotBlank(message = "Password is mandatory")
    private String password;

    @JsonIgnore
    @Column(columnDefinition = "integer default 0")
    private BigDecimal balance;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;
}
