package com.bank.cards.domain.valueobject;

public record CardNumber(String value) {

    public CardNumber {

        value = value.replaceAll("[\\s-]", "");

        if (!value.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be exactly 16 digits");
        }

        if (!isValidLuhn(value)) {
            throw new IllegalArgumentException("Card number failed Luhn validation");
        }
    }

    private static boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    public String masked() {
        return "****-****-****-" + value.substring(12);
    }

    public String lastFourDigits() {
        return value.substring(12);
    }
}
