package com.bank.cards.application.dto.input;

import com.bank.cards.domain.valueobject.TransactionId;
import com.bank.cards.domain.valueobject.TransactionStatus;

public record UpdateTransactionStatusCommand(
        TransactionId transactionId,
        TransactionStatus newStatus
) {}