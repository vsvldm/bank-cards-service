package com.bank.cards.infrastructure.persistence.adapter;

import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.*;
import com.bank.cards.infrastructure.persistence.entity.RoleJpaEntity;
import com.bank.cards.infrastructure.persistence.entity.UserJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = toJpaEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value()).map(this::toDomainEntity);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsername(username.value()).map(this::toDomainEntity);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return jpaRepository.existsByUsername(username.value());
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<User> findAll(int page, int size, String sort) {
        Sort sortOrder = Sort.by(Sort.Direction.ASC, sort);
        return jpaRepository.findAll(PageRequest.of(page, size, sortOrder))
                .getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    private UserJpaEntity toJpaEntity(User user) {
        Set<RoleJpaEntity> roleEntities = user.getRoles().stream()
                .map(role -> RoleJpaEntity.builder()
                        .id(role.getId() != null ? role.getId().value() : null)
                        .name(role.getName().value())
                        .build())
                .collect(Collectors.toSet());

        return UserJpaEntity.builder()
                .id(user.getId() != null ? user.getId().value() : null)
                .username(user.getUsername().value())
                .password(user.getPassword().value())
                .email(user.getEmail().value())
                .roles(roleEntities)
                .build();
    }

    private User toDomainEntity(UserJpaEntity entity) {
        Set<Role> roles = entity.getRoles().stream()
                .map(roleEntity -> Role.reconstitute(
                        new RoleId(roleEntity.getId()),
                        new RoleName(roleEntity.getName())
                ))
                .collect(Collectors.toSet());

        return User.reconstitute(
                new UserId(entity.getId()),
                new Username(entity.getUsername()),
                new EncodedPassword(entity.getPassword()),
                new Email(entity.getEmail()),
                roles
        );
    }
}