package com.bank.cards.presentation.dto.request;

import com.bank.cards.domain.valueobject.CardStatus;
import jakarta.validation.constraints.NotNull;

public record ModerateStatusRequest(
        @NotNull(message = "Status is required")
        CardStatus status
) {}