package com.lutzapi.infrastructure.repositories;

import com.lutzapi.domain.entities.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByDocument(String document);
    Optional<User> findByEmail(String document);
}
