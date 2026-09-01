package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.usecase.AdminDeleteCardUseCase;
import com.bank.cards.application.usecase.GetAllCardsUseCase;
import com.bank.cards.application.usecase.ModerateCardStatusUseCase;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.CardStatus;
import com.bank.cards.presentation.dto.request.ModerateStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/v1/cards")
@RequiredArgsConstructor
public class AdminCardController {

    private final GetAllCardsUseCase getAllCardsUseCase;
    private final ModerateCardStatusUseCase moderateCardStatusUseCase;
    private final AdminDeleteCardUseCase adminDeleteCardUseCase;

    @GetMapping
    public ResponseEntity<PageResponse<CardResponse>> getAllCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<CardResponse> response = getAllCardsUseCase.execute(status, username, page, size);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cardId}/status")
    public ResponseEntity<Void> moderateStatus(
            @PathVariable String cardId,
            @Valid @RequestBody ModerateStatusRequest request) {

        moderateCardStatusUseCase.execute(
                CardId.fromString(cardId),
                request.status()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardId) {
        adminDeleteCardUseCase.execute(CardId.fromString(cardId));
        return ResponseEntity.noContent().build();
    }
}