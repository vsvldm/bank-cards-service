package com.bank.cards.presentation.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
        String username,

        @Pattern(regexp = "\\S+", message = "Password must not contain spaces")
        @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
        String password,

        @Pattern(regexp = "\\S+", message = "Confirm password must not contain spaces")
        @Size(min = 6, max = 100, message = "Confirm password must be 6-100 characters")
        String confirmPassword,

        @Email(message = "Email format is invalid")
        String email
) {
    @AssertTrue(message = "Both password fields must be provided or omitted together")
    public boolean isPasswordConsistent() {
        return (password == null && confirmPassword == null) ||
                (password != null && confirmPassword != null);
    }

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordsMatch() {
        return password == null || confirmPassword == null || password.equals(confirmPassword);
    }
}