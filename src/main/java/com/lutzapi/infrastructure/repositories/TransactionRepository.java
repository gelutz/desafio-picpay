package com.lutzapi.infrastructure.repositories;

import com.lutzapi.domain.entities.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
