package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.domain.port.EncryptionPort;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCardsUseCase {

    private final CardRepository cardRepository;
    private final UserQueryPort userQueryPort;
    private final EncryptionPort encryptionPort;

    @Transactional(readOnly = true)
    public List<CardResponse> executeByUsername(String username, int page, int size) {
        UserId userId = userQueryPort.getUserIdByUsername(username);
        return cardRepository.findByUserId(userId, page, size).stream()
                .map(card -> CardResponse.from(card, encryptionPort))
                .toList();
    }
}