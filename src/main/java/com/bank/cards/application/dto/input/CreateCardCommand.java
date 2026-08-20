package com.bank.cards.application.dto.input;

import com.bank.cards.domain.valueobject.CardNumber;
import com.bank.cards.domain.valueobject.ExpiryDate;

public record CreateCardCommand(
        String username,
        CardNumber cardNumber,
        String cvv,
        ExpiryDate expiryDate
) {}
