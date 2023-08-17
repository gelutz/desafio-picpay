package com.lutzapi.application.services;

import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User createUser(UserDTO user) {
        User newUser = new User();
        newUser.setFirstName(user.firstName());
        newUser.setLastName(user.lastName());
        newUser.setDocument(user.document());
        newUser.setEmail(user.email());
        newUser.setType(user.type());
        newUser.setBalance(user.balance());

        return userRepository.save(newUser);
    }

    public void validateTransaction(User buyer, BigDecimal amount) throws Exception {
        if (buyer.getType() == UserType.SELLER) {
            // TODO: Alterar essa Exception para uma mais específica
            throw new Exception("Usuários do tipo SELLER não podem enviar transações");
        }

        if (buyer.getBalance().compareTo(amount) < 0) {
            // TODO: Alterar essa Exception para uma mais específica
            throw new Exception("Saldo insuficiente");
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
