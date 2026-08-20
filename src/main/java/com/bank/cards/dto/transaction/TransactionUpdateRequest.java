package com.bank.cards.dto.transaction;

import com.bank.cards.entity.transaction.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionUpdateRequest {
    @NotNull(message = "The transaction status is required")
    private TransactionStatus status;
}
