package com.bank.cards.presentation.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Pattern(regexp = "\\S+", message = "Password must not contain spaces")
        @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
        String password,

        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email format is invalid")
        String email
) {
    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordsMatch() {
        return password != null && confirmPassword != null && password.equals(confirmPassword);
    }
}