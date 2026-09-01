package com.bank.cards.infrastructure.persistence.adapter;

import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.*;
import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity;
import com.bank.cards.infrastructure.persistence.repository.JpaCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepository {

    private final JpaCardRepository jpaRepository;

    @Override
    public BankCard save(BankCard card) {
        CardJpaEntity jpaEntity = toJpaEntity(card);
        CardJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<BankCard> findById(CardId id) {
        return jpaRepository.findById(id.value()).map(this::toDomainEntity);
    }

    @Override
    public List<BankCard> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value()).stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public long countByUserId(UserId userId) {
        return jpaRepository.countByUserId(userId.value());
    }

    @Override
    public boolean existsById(CardId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(CardId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<BankCard> findByUserId(UserId userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByUserId(userId.value(), pageable)
                .getContent()
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<BankCard> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<BankCard> findByStatus(CardStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatus(status.name(), pageable)
                .getContent()
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public List<BankCard> findByUserIdAndStatus(UserId userId, CardStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByUserIdAndStatus(userId.value(), status.name(), pageable)
                .getContent()
                .stream()
                .map(this::toDomainEntity)
                .toList();
    }

    @Override
    public long countAll() {
        return jpaRepository.count();
    }

    @Override
    public long countByStatus(CardStatus status) {
        return jpaRepository.countByStatus(status.name());
    }

    @Override
    public long countByUserIdAndStatus(UserId userId, CardStatus status) {
        return jpaRepository.countByUserIdAndStatus(userId.value(), status.name());
    }

    private CardJpaEntity toJpaEntity(BankCard card) {
        return CardJpaEntity.builder()
                .id(card.getId().value())
                .userId(card.getUserId().value())
                .encryptedCardNumber(card.getEncryptedCardNumber().value())
                .encryptedCvv(card.getEncryptedCvv().value())
                .expiryDate(card.getExpiryDate().value().atDay(1))
                .balance(card.getBalance())
                .status(card.getStatus().name())
                .build();
    }

    private BankCard toDomainEntity(CardJpaEntity entity) {
        return BankCard.reconstitute(
                CardId.from(entity.getId()),
                new UserId(entity.getUserId()),
                new EncryptedData(entity.getEncryptedCardNumber()),
                new EncryptedData(entity.getEncryptedCvv()),
                new ExpiryDate(java.time.YearMonth.from(entity.getExpiryDate())),
                entity.getBalance(),
                CardStatus.valueOf(entity.getStatus())
        );
    }
}