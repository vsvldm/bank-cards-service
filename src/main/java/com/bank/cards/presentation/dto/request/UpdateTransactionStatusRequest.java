package com.bank.cards.presentation.dto.request;

import com.bank.cards.domain.valueobject.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTransactionStatusRequest(
        @NotNull(message = "Status is required")
        TransactionStatus status
) {}