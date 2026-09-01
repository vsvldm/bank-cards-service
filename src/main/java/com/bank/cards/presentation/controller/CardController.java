package com.bank.cards.presentation.controller;

import com.bank.cards.application.dto.input.CreateCardCommand;
import com.bank.cards.application.dto.input.TransferCommand;
import com.bank.cards.application.dto.output.CardResponse;
import com.bank.cards.application.dto.output.PageResponse;
import com.bank.cards.application.port.UserQueryPort;
import com.bank.cards.application.usecase.*;
import com.bank.cards.domain.valueobject.CardId;
import com.bank.cards.domain.valueobject.CardNumber;
import com.bank.cards.domain.valueobject.ExpiryDate;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.presentation.dto.request.CreateCardRequest;
import com.bank.cards.presentation.dto.request.TransferRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CreateCardUseCase createCardUseCase;
    private final BlockCardUseCase blockCardUseCase;
    private final ActivateCardUseCase activateCardUseCase;
    private final DeleteCardUseCase deleteCardUseCase;
    private final GetCardsUseCase getCardsUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final UserQueryPort userQueryPort;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            Principal principal,
            @Valid @RequestBody CreateCardRequest request) {

        CreateCardCommand command = new CreateCardCommand(
                principal.getName(),
                new CardNumber(request.cardNumber()),
                request.cvv(),
                ExpiryDate.fromString(request.expiryDate())
        );

        CardResponse response = createCardUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-cards")
    public ResponseEntity<PageResponse<CardResponse>> getMyCards(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<CardResponse> response = getCardsUseCase.executeByUsername(
                principal.getName(), page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(
            Principal principal,
            @PathVariable String cardId) {

        UserId userId = userQueryPort.getUserIdByUsername(principal.getName());
        blockCardUseCase.execute(CardId.fromString(cardId), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cardId}/activate")
    public ResponseEntity<Void> activateCard(
            Principal principal,
            @PathVariable String cardId) {

        UserId userId = userQueryPort.getUserIdByUsername(principal.getName());
        activateCardUseCase.execute(CardId.fromString(cardId), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            Principal principal,
            @PathVariable String cardId) {

        UserId userId = userQueryPort.getUserIdByUsername(principal.getName());
        deleteCardUseCase.execute(CardId.fromString(cardId), userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            Principal principal,
            @Valid @RequestBody TransferRequest request) {
        UserId userId = userQueryPort.getUserIdByUsername(principal.getName());
        TransferCommand command = new TransferCommand(
                CardId.fromString(request.fromCardId()),
                CardId.fromString(request.toCardId()),
                request.amount()
        );
        transferMoneyUseCase.execute(command, userId);
        return ResponseEntity.ok().build();
    }
}