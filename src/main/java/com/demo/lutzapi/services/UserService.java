package com.demo.lutzapi.services;

import com.demo.lutzapi.domain.user.User;
import com.demo.lutzapi.domain.user.UserType;
import com.demo.lutzapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getType() == UserType.BUYER) {
            throw new Exception("Usuários do tipo BUYER não podem enviar transações");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
