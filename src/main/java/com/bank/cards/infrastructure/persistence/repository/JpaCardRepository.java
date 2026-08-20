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

    List<CardJpaEntity> findByUserId(Long userId);

    Page<CardJpaEntity> findByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    Page<CardJpaEntity> findByStatus(String status, Pageable pageable);

    Page<CardJpaEntity> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
}