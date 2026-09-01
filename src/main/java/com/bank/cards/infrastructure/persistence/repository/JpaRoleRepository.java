package com.bank.cards.infrastructure.persistence.repository;

import com.bank.cards.infrastructure.persistence.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRoleRepository extends JpaRepository<RoleJpaEntity, UUID> {
    
    Optional<RoleJpaEntity> findByName(String name);
    
    boolean existsByName(String name);
}