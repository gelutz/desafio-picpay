package com.lutzapi.infrastructure.repositories;

import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByBuyerOrSeller(User buyer, User seller);
    Optional<Transaction> findByIdAndDeletedIsTrue(Long id);
}
