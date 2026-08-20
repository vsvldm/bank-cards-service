
package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.domain.port.EncryptionPort;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.CardStatus;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCardsUseCase {

    private final CardRepository cardRepository;
    private final EncryptionPort encryptionPort;
    private final UserQueryPort userQueryPort;

    @Transactional(readOnly = true)
    public List<CardResponse> execute(CardStatus status, String username, int page, int size) {

        if (username != null && status != null) {
            UserId userId = userQueryPort.getUserIdByUsername(username);
            return cardRepository.findByUserIdAndStatus(userId, status, page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
        }

        if (username != null) {
            UserId userId = userQueryPort.getUserIdByUsername(username);
            return cardRepository.findByUserId(userId).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
        }

        if (status != null) {
            return cardRepository.findByStatus(status, page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
        }

        return cardRepository.findAll(page, size).stream()
                .map(card -> CardResponse.from(card, encryptionPort))
                .toList();
    }
}