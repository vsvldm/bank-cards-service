package com.bank.cards.application.usecase;

import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModerateCardStatusUseCase {

    private final CardRepository cardRepository;

    @Transactional
    public void execute(CardId cardId, CardStatus newStatus) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        switch (newStatus) {
            case ACTIVE -> card.activate();
            case BLOCKED -> card.block();
            case EXPIRED -> throw new IllegalArgumentException(
                    "Cannot manually set EXPIRED status. Cards expire automatically.");
        }

        cardRepository.save(card);
    }
}