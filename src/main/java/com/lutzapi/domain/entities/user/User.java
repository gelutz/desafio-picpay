package com.lutzapi.domain.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String document;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("BUYER")
    private UserType type;

    @JsonIgnore
    @Column
    @ColumnDefault("0")
    private BigDecimal balance;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;
}
