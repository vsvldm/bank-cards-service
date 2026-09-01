package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.dto.output.PageResponse;
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
    public PageResponse<CardResponse> execute(CardStatus status, String username, int page, int size) {
        List<CardResponse> content;
        long totalElements;

        if (username != null && status != null) {
            UserId userId = userQueryPort.getUserIdByUsername(username);
            content = cardRepository.findByUserIdAndStatus(userId, status, page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
            totalElements = cardRepository.countByUserIdAndStatus(userId, status);
        } else if (username != null) {
            UserId userId = userQueryPort.getUserIdByUsername(username);
            content = cardRepository.findByUserId(userId, page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
            totalElements = cardRepository.countByUserId(userId);
        } else if (status != null) {
            content = cardRepository.findByStatus(status, page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
            totalElements = cardRepository.countByStatus(status);
        } else {
            content = cardRepository.findAll(page, size).stream()
                    .map(card -> CardResponse.from(card, encryptionPort))
                    .toList();
            totalElements = cardRepository.countAll();
        }

        return PageResponse.of(content, page, size, totalElements);
    }
}