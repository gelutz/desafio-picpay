package com.lutzapi.domain.entities.transaction;

import com.lutzapi.domain.entities.user.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity(name = "transactions")
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE table_product SET deleted = true WHERE id=?")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="buyerId")
    private User buyer;

    @ManyToOne
    @JoinColumn(name="sellerId")
    private User seller;

    private BigDecimal amount;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    private boolean deleted = false;

    public static Transaction fromDTO(TransactionDTO dto) {
        return builder()
                .buyer(dto.buyer())
                .seller(dto.seller())
                .amount(dto.amount())
                .build();
    }
}
