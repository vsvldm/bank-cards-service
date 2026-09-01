package com.bank.cards.application.usecase;

import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.exception.UserNotFoundException;
import com.bank.cards.domain.exception.UserOperationException;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.UserId;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(String targetUserId, String initiatorUsername) {
        UserId targetId = UserId.fromString(targetUserId);

        User targetUser = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + targetUserId + " not found"));

        User initiator = userRepository.findByUsername(new Username(initiatorUsername))
                .orElseThrow(() -> new UserNotFoundException("Initiator user not found"));

        if (initiator.getId().value().equals(targetUser.getId().value())) {
            throw new UserOperationException("User cannot delete himself.");
        }

        userRepository.deleteById(targetId);
    }
}