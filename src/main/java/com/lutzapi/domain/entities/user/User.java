package com.lutzapi.domain.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String username;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String document;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    //    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType type = UserType.BUYER;

    @JsonIgnore
    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    // model não deveria importar o serviço do BCrypt direto, quebra a clean architecture
    // TODO: ajustar
    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public static class UserBuilder {
        // TODO: mesma situação do TODO acima, ajustar
        public UserBuilder password(String password) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(password);
            return this;
        }
    }

}
