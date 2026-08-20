    package com.bank.cards.mapper;

    import com.bank.cards.dto.user.UserRegistrationRequest;
    import com.bank.cards.dto.user.UserResponse;
    import com.bank.cards.entity.user.User;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Component;

    import java.util.HashSet;

    @Component
    @RequiredArgsConstructor
    public class UserMapper {
        private final PasswordEncoder passwordEncoder;

        public User toUser(UserRegistrationRequest request) {
            return User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .roles(new HashSet<>())
                    .build();
        }

        public UserResponse toUserResponse(User user) {
            return UserResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
    }
