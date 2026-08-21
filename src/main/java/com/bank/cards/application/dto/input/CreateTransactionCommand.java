package com.bank.cards.application.dto.input;

import com.bank.cards.domain.valueobject.CardId;

import java.math.BigDecimal;

public record CreateTransactionCommand(
        CardId sourceCardId,
        CardId targetCardId,
        BigDecimal amount
) {}