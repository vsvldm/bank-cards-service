package com.bank.cards.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCardRequest(
        @NotBlank(message = "Card number is required")
        @Pattern(
                regexp = "\\d{16}|\\d{4}[\\s-]\\d{4}[\\s-]\\d{4}[\\s-]\\d{4}",
                message = "Card number must be 16 digits"
        )
        String cardNumber,

        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
        String cvv,

        @NotBlank(message = "Expiry date is required")
        @Pattern(
                regexp = "(0[1-9]|1[0-2])/\\d{2}",
                message = "Expiry date must be in MM/yy format"
        )
        String expiryDate
) {}