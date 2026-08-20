package com.bank.cards.domain.entity;

import com.bank.cards.domain.exception.CardBlockedException;
import com.bank.cards.domain.exception.InsufficientFundsException;
import com.bank.cards.domain.valueobject.*;

import java.math.BigDecimal;

public class BankCard {

    private final CardId id;
    private final UserId userId;
    private final EncryptedData encryptedCardNumber;
    private final EncryptedData encryptedCvv;
    private final ExpiryDate expiryDate;
    private BigDecimal balance;
    private CardStatus status;

    private BankCard(CardId id, UserId userId, EncryptedData encryptedCardNumber,
                     EncryptedData encryptedCvv, ExpiryDate expiryDate,
                     BigDecimal balance, CardStatus status) {
        this.id = id;
        this.userId = userId;
        this.encryptedCardNumber = encryptedCardNumber;
        this.encryptedCvv = encryptedCvv;
        this.expiryDate = expiryDate;
        this.balance = balance;
        this.status = status;
    }

    public static BankCard createNew(UserId userId, EncryptedData encryptedCardNumber,
                                     EncryptedData encryptedCvv, ExpiryDate expiryDate) {
        return new BankCard(
                CardId.generate(),
                userId,
                encryptedCardNumber,
                encryptedCvv,
                expiryDate,
                BigDecimal.ZERO,
                CardStatus.ACTIVE
        );
    }

    public static BankCard reconstitute(CardId id, UserId userId,
                                        EncryptedData encryptedCardNumber,
                                        EncryptedData encryptedCvv,
                                        ExpiryDate expiryDate,
                                        BigDecimal balance,
                                        CardStatus status) {
        return new BankCard(id, userId, encryptedCardNumber, encryptedCvv,
                expiryDate, balance, status);
    }

    public void block() {
        if (this.status == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }
        this.status = CardStatus.BLOCKED;
    }

    public void activate() {
        if (this.status != CardStatus.BLOCKED) {
            throw new IllegalStateException("Only blocked cards can be activated");
        }
        this.status = CardStatus.ACTIVE;
    }

    public void withdraw(BigDecimal amount) {
        if (!isActive()) {
            throw new CardBlockedException(this.id);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(this.id, this.balance, amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if (!isActive()) {
            throw new CardBlockedException(this.id);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public boolean isActive() {
        return this.status == CardStatus.ACTIVE && !this.expiryDate.isExpired();
    }

    public boolean isExpired() {
        return this.expiryDate.isExpired();
    }

    public CardId getId() { return id; }
    public UserId getUserId() { return userId; }
    public EncryptedData getEncryptedCardNumber() { return encryptedCardNumber; }
    public EncryptedData getEncryptedCvv() { return encryptedCvv; }
    public ExpiryDate getExpiryDate() { return expiryDate; }
    public BigDecimal getBalance() { return balance; }
    public CardStatus getStatus() { return status; }
}