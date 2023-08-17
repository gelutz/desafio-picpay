package com.lutzapi.infrastructure.repositories;

import com.lutzapi.domain.entities.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
