package com.bank.cards.infrastructure.persistence.adapter;

import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.valueobject.RoleId;
import com.bank.cards.domain.valueobject.RoleName;
import com.bank.cards.infrastructure.persistence.entity.RoleJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
    
    private final JpaRoleRepository jpaRepository;

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = toJpaEntity(role);
        RoleJpaEntity saved = jpaRepository.save(entity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return jpaRepository.findById(id.value()).map(this::toDomainEntity);
    }

    @Override
    public Optional<Role> findByName(RoleName name) {
        return jpaRepository.findByName(name.value()).map(this::toDomainEntity);
    }

    @Override
    public boolean existsByName(RoleName name) {
        return jpaRepository.existsByName(name.value());
    }

    @Override
    public void deleteById(RoleId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<Role> findAll(int page, int size, String sort) {
        Sort sortOrder = Sort.by(Sort.Direction.ASC, sort);
        return jpaRepository.findAll(PageRequest.of(page, size, sortOrder))
                .getContent().stream()
                .map(this::toDomainEntity)
                .toList();
    }

    private RoleJpaEntity toJpaEntity(Role role) {
        return RoleJpaEntity.builder()
                .id(role.getId() != null ? role.getId().value() : null)
                .name(role.getName().value())
                .build();
    }

    private Role toDomainEntity(RoleJpaEntity entity) {
        return Role.reconstitute(
                new RoleId(entity.getId()),
                new RoleName(entity.getName())
        );
    }
}