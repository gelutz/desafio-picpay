package com.lutzapi.domain.entities.transaction;

import com.lutzapi.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
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
}
