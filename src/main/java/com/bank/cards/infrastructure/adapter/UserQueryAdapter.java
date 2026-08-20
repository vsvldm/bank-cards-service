package com.bank.cards.infrastructure.adapter;

import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {
    private final UserRepository userRepository;

    @Override
    public UserId getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserId(user.getId()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));
    }
}
