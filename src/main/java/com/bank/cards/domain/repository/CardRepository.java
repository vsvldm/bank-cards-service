package com.bank.cards.domain.repository;

import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.CardStatus;
import com.bank.cards.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface CardRepository {

    BankCard save(BankCard card);

    Optional<BankCard> findById(CardId id);

    List<BankCard> findByUserId(UserId userId);

    List<BankCard> findByUserId(UserId userId, int page, int size);

    long countByUserId(UserId userId);

    boolean existsById(CardId id);

    void deleteById(CardId id);

    List<BankCard> findAll(int page, int size);

    List<BankCard> findByStatus(CardStatus status, int page, int size);

    List<BankCard> findByUserIdAndStatus(UserId userId, CardStatus status, int page, int size);
}
