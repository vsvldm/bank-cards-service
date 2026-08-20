package com.bank.cards.mapper;

import com.bank.cards.dto.transaction.TransactionRequest;
import com.bank.cards.dto.transaction.TransactionResponse;
import com.bank.cards.dto.transaction.TransactionFullResponse;
import com.bank.cards.infrastructure.persistence.entity.CardJpaEntity; // <-- НОВЫЙ ИМПОРТ
import com.bank.cards.entity.transaction.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toTransaction(TransactionRequest request, CardJpaEntity sourceCard, CardJpaEntity targetCard) {
        return Transaction.builder()
                .sourceCard(sourceCard)
                .targetCard(targetCard)
                .amount(request.getAmount())
                .build();
    }

    public TransactionResponse toTransactionResponse(Transaction transaction, String message) {
        return TransactionResponse.builder()
                .status(transaction.getStatus())
                .message(message)
                .build();
    }

    public TransactionFullResponse toFullResponse(Transaction transaction) {
        // TODO: В будущем здесь нужно внедрить EncryptionPort, чтобы расшифровать номер перед маскированием
        String maskedSource = transaction.getSourceCard() != null ? "****-****-****-****" : "N/A";
        String maskedTarget = transaction.getTargetCard() != null ? "****-****-****-****" : "N/A";

        return TransactionFullResponse.builder()
                .id(transaction.getId())
                .sourceCardNumber(maskedSource)
                .targetCardNumber(maskedTarget)
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .status(transaction.getStatus())
                .build();
    }
}