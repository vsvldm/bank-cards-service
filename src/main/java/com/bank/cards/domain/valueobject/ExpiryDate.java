package com.bank.cards.domain.valueobject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record ExpiryDate(YearMonth value) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    public ExpiryDate {
        if (value == null) {
            throw new IllegalArgumentException("Expiry date cannot be null");
        }
    }

    public static ExpiryDate fromString(String expiryDate) {
        try {
            YearMonth yearMonth = YearMonth.parse(expiryDate, FORMATTER);

            if (yearMonth.isBefore(YearMonth.now())) {
                throw new IllegalArgumentException("Card is expired");
            }

            if (yearMonth.isAfter(YearMonth.now().plusYears(10))) {
                throw new IllegalArgumentException("Expiry date too far in future");
            }

            return new ExpiryDate(yearMonth);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiry date format. Expected: MM/yy");
        }
    }

    public boolean isExpired() {
        return value.isBefore(YearMonth.now());
    }

    public String formatted() {
        return value.format(FORMATTER);
    }
}
