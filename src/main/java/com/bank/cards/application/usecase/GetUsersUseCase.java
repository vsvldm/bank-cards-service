package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.exception.UserNotFoundException;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUsersUseCase {

    private final UserRepository userRepository;

    public UserResponse getById(String id) {
        UserId userId = UserId.fromString(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + id + " not found"));
        return UserResponse.from(user);
    }

    public UserResponse getByUsername(String username) {
        Username usernameVO = new Username(username);
        User user = userRepository.findByUsername(usernameVO)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return UserResponse.from(user);
    }

    public List<UserResponse> getAll(int page, int size, String sort) {
        return userRepository.findAll(page, size, sort).stream()
                .map(UserResponse::from)
                .toList();
    }
}