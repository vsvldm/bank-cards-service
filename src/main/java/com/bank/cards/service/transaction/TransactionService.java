package com.bank.cards.service.transaction;

import com.bank.cards.dto.transaction.TransactionFullResponse;
import com.bank.cards.dto.transaction.TransactionRequest;
import com.bank.cards.dto.transaction.TransactionResponse;
import com.bank.cards.dto.transaction.TransactionUpdateRequest;
import com.bank.cards.entity.transaction.TransactionStatus;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
    TransactionResponse updateStatusTransaction(Long transactionId, TransactionUpdateRequest transactionUpdateRequest);
    List<TransactionFullResponse> getTransactions(UUID sourceCardId, TransactionStatus status, Pageable pageable);
    List<TransactionFullResponse> getTransactionsByCard(UUID cardId, TransactionStatus status, Pageable pageable);
}