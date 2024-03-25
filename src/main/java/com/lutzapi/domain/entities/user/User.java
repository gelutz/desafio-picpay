package com.lutzapi.domain.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String tenantId;

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType type = UserType.BUYER;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    public void subtractBalance(BigDecimal amount) {
        if (getBalance().compareTo(amount) < 0) throw new InsufficientFundsException(getId(), amount, getBalance());
        setBalance(getBalance().subtract(amount));
    }

    public void addBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("O valor não pode ser negativo/zero.");
        setBalance(getBalance().add(amount));
    }

    public static User fromDTO(UserDTO dto) {
        return builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .type(dto.type())
                .document(dto.document())
                .balance(dto.balance())
                .build();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
