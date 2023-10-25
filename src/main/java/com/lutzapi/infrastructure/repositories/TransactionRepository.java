package com.lutzapi.infrastructure.repositories;

import com.lutzapi.application.dtos.TransactionByUserDTO;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<TransactionByUserDTO> findAllByBuyerOrSeller(User buyer, User seller);
}
