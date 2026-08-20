package com.bank.cards.application.usecase;

import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.CardId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDeleteCardUseCase {

    private final CardRepository cardRepository;

    @Transactional
    public void execute(CardId cardId) {

        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException(cardId);
        }

        cardRepository.deleteById(cardId);
    }
}