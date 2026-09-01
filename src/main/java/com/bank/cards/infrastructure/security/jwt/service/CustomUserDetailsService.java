package com.bank.cards.infrastructure.security.jwt.service;

import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(new Username(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername().value(),
                user.getPassword().value(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().value()))
                        .toList()
        );
    }
}