package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.UpdateUserProfileCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.exception.UserNotFoundException;
import com.bank.cards.domain.exception.UsernameAlreadyExistsException;
import com.bank.cards.domain.port.PasswordEncoderPort;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.Email;
import com.bank.cards.domain.valueobject.EncodedPassword;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Transactional
    public UserResponse execute(UpdateUserProfileCommand command) {
        User user = userRepository.findByUsername(new Username(command.currentUsername()))
                .orElseThrow(() -> new UserNotFoundException("User not found: " + command.currentUsername()));

        Username newUsername = null;
        if (command.newUsername() != null && !command.newUsername().equals(user.getUsername().value())) {
            newUsername = new Username(command.newUsername());
            if (userRepository.existsByUsername(newUsername)) {
                throw new UsernameAlreadyExistsException(newUsername.value());
            }
        }

        EncodedPassword newPassword = null;
        if (command.newPassword() != null) {
            newPassword = passwordEncoderPort.encode(command.newPassword());
        }

        Email newEmail = null;
        if (command.newEmail() != null) {
            newEmail = new Email(command.newEmail());
        }

        user.updateProfile(newUsername, newPassword, newEmail);
        User updatedUser = userRepository.save(user);

        return UserResponse.from(updatedUser);
    }
}