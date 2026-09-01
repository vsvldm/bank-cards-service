package com.bank.cards.application.usecase;

import com.bank.cards.application.dto.input.RegisterUserCommand;
import com.bank.cards.application.dto.output.UserResponse;
import com.bank.cards.domain.entity.Role;
import com.bank.cards.domain.entity.User;
import com.bank.cards.domain.exception.RoleNotFoundException;
import com.bank.cards.domain.exception.UsernameAlreadyExistsException;
import com.bank.cards.domain.port.PasswordEncoderPort;
import com.bank.cards.domain.repository.RoleRepository;
import com.bank.cards.domain.repository.UserRepository;
import com.bank.cards.domain.valueobject.Email;
import com.bank.cards.domain.valueobject.EncodedPassword;
import com.bank.cards.domain.valueobject.RoleName;
import com.bank.cards.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Transactional
    public UserResponse execute(RegisterUserCommand command) {
        Username username = new Username(command.username());

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username.value());
        }

        Email email = new Email(command.email());
        EncodedPassword encodedPassword = passwordEncoderPort.encode(command.password());

        RoleName defaultRoleName = new RoleName(Role.DEFAULT_ROLE_NAME);
        Role defaultRole = roleRepository.findByName(defaultRoleName)
                .orElseThrow(() -> new RoleNotFoundException("System role not found: " + Role.DEFAULT_ROLE_NAME));

        User newUser = User.createNew(username, encodedPassword, email, defaultRole);
        User savedUser = userRepository.save(newUser);

        return UserResponse.from(savedUser);
    }
}