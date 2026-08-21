package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.dto.output.TransactionResponse;
import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetTransactionsByCardUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionsByCardUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public PageResponse<TransactionResponse> execute(CardId cardId, int page, int size) {
        List<Transaction> transactions = transactionRepository.findBySourceCardIdOrTargetCardId(cardId, page, size);
        long totalElements = transactionRepository.countBySourceCardIdOrTargetCardId(cardId);

        List<TransactionResponse> content = transactions.stream()
                .map(TransactionResponse::from)
                .toList();

        return PageResponse.of(content, page, size, totalElements);
    }
}