package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.dto.output.TransactionResponse;
import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.application.usecase.GetTransactionsByCardUseCase;
import com.bank.cards.domain.entity.BankCard;
import com.bank.cards.domain.exception.CardNotFoundException;
import com.bank.cards.domain.repository.CardRepository;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final GetTransactionsByCardUseCase getTransactionsByCardUseCase;
    private final CardRepository cardRepository;
    private final UserQueryPort userQueryPort;

    @GetMapping
    public ResponseEntity<PageResponse<TransactionResponse>> getMyTransactions(
            Principal principal,
            @RequestParam String cardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UserId userId = userQueryPort.getUserIdByUsername(principal.getName());
        CardId cId = CardId.fromString(cardId);

        BankCard card = cardRepository.findById(cId)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardId));

        if (!card.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to user");
        }

        PageResponse<TransactionResponse> response = getTransactionsByCardUseCase.execute(cId, page, size);
        return ResponseEntity.ok(response);
    }
}