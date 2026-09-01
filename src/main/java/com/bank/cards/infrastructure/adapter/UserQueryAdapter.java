package com.bank.cards.infrastructure.adapter;

import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort {
    private final UserRepository userRepository;

    @Override
    public UserId getUserIdByUsername(String username) {
        return userRepository.findByUsername(new Username(username))
                .map(user -> new UserId(user.getId().value()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));
    }
}
