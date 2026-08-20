package com.bank.cards.dto.transaction;

import com.bank.cards.entity.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private TransactionStatus status;
    private String message;
}
