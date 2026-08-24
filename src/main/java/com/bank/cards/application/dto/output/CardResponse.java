package com.bank.cards.application.dto.output;

import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.port.EncryptionPort;
import com.bank.cards.domain.valueobject.CardNumber;
import com.bank.cards.domain.valueobject.CardStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CardResponse(
        UUID id,
        Long userId,
        String maskedCardNumber,
        String expiryDate,
        BigDecimal balance,
        CardStatus status
) {
    public static CardResponse from(BankCard card, EncryptionPort encryptionPort) {
        String decrypted = encryptionPort.decrypt(card.getEncryptedCardNumber());
        CardNumber cardNumber = new CardNumber(decrypted);
        String masked = cardNumber.masked();

        return new CardResponse(
                card.getId().value(),
                card.getUserId().value(),
                masked,
                card.getExpiryDate().formatted(),
                card.getBalance(),
                card.getStatus()
        );
    }
}