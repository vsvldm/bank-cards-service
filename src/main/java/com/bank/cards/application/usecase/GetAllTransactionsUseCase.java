package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.dto.output.TransactionFullResponse;
import com.bank.cards.domain.entity.Transaction;
import com.bank.cards.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllTransactionsUseCase {

    private final TransactionRepository transactionRepository;

    public GetAllTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public PageResponse<TransactionFullResponse> execute(int page, int size) {
        List<Transaction> transactions = transactionRepository.findAll(page, size);
        long totalElements = transactionRepository.countAll();

        List<TransactionFullResponse> content = transactions.stream()
                .map(TransactionFullResponse::from)
                .toList();

        return PageResponse.of(content, page, size, totalElements);
    }
}