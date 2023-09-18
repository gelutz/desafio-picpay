package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer ID", id + ""));
    }

    public User createUser(UserDTO user) {
        validateUserData(user);
        User newUser = new User();
        newUser.setFirstName(user.firstName());
        newUser.setLastName(user.lastName());
        newUser.setDocument(user.document());
        newUser.setEmail(user.email());
        newUser.setType(user.type());
        newUser.setBalance(user.balance() != null ? user.balance() : BigDecimal.valueOf(0));

        return userRepository.save(newUser);
    }

    public void validateUserData(UserDTO user) {
        List<String> emptyFields = new ArrayList<>();
        if (StringUtils.isEmpty(user.firstName())) emptyFields.add("First name");
        if (StringUtils.isEmpty(user.document())) emptyFields.add("Document");
        if (user.type() == null) emptyFields.add("Type");

        if (!emptyFields.isEmpty()) throw new MissingDataException(emptyFields);
    }

    public void validateUserForTransaction(User buyer, BigDecimal amount) {
        if (buyer.getType() == UserType.SELLER) {
            throw new WrongUserTypeException(buyer.getId());
        }

        if (buyer.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(buyer.getId());
        }
    }
}
