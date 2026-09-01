package com.bank.cards.application.usecase;

import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.exception.CardOwnershipException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateCardUseCase {

    private final CardRepository cardRepository;

    @Transactional
    public void execute(CardId cardId, UserId ownerId) {
        BankCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        if (!card.getUserId().equals(ownerId)) {
            throw new CardOwnershipException("Card does not belong to user");        }

        card.activate();
        cardRepository.save(card);
    }
}