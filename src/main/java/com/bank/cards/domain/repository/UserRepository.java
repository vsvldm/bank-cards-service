package com.bank.cards.domain.repository;

import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.domain.valueobject.Username;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(Username username);

    boolean existsByUsername(Username username);

    void deleteById(UserId id);

    List<User> findAll(int page, int size, String sort);
}