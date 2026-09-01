package com.bank.cards.infrastructure.persistence.repository;

import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCardRepository extends JpaRepository<CardJpaEntity, UUID> {

    List<CardJpaEntity> findByUserId(UUID userId);

    Page<CardJpaEntity> findByUserId(UUID userId, Pageable pageable);

    long countByUserId(UUID userId);

    Page<CardJpaEntity> findByStatus(String status, Pageable pageable);

    long countByStatus(String status);

    Page<CardJpaEntity> findByUserIdAndStatus(UUID userId, String status, Pageable pageable);

    long countByUserIdAndStatus(UUID userId, String status);
}