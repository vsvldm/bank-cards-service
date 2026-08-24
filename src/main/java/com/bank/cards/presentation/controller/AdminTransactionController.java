package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.UpdateTransactionStatusCommand;
import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.dto.output.TransactionFullResponse;
import com.bank.cards.application.dto.output.TransactionResponse;
import com.bank.cards.application.usecase.GetAllTransactionsUseCase;
import com.bank.cards.application.usecase.UpdateTransactionStatusUseCase;
import com.bank.cards.domain.valueobject.TransactionId;
import com.bank.cards.presentation.dto.request.UpdateTransactionStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/api/v1/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {

    private final GetAllTransactionsUseCase getAllTransactionsUseCase;
    private final UpdateTransactionStatusUseCase updateTransactionStatusUseCase;

    @GetMapping
    public ResponseEntity<PageResponse<TransactionFullResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(getAllTransactionsUseCase.execute(page, size));
    }

    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<TransactionResponse> updateStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateTransactionStatusRequest request) {

        UpdateTransactionStatusCommand command = new UpdateTransactionStatusCommand(
                new TransactionId(UUID.fromString(transactionId)),
                request.status()
        );

        TransactionResponse response = updateTransactionStatusUseCase.execute(command);
        return ResponseEntity.ok(response);
    }
}