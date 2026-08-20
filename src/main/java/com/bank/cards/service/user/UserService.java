package com.bank.cards.service.user;

import com.bank.cards.dto.user.ChangeRoleRequest;
import com.bank.cards.dto.user.UserRegistrationRequest;
import com.bank.cards.dto.user.UserResponse;
import com.bank.cards.dto.user.UserUpdateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {
    UserResponse create(UserRegistrationRequest userRegistrationRequest);

    UserResponse changeUserRole(Principal principal, ChangeRoleRequest changeRoleRequest);

    UserResponse update(Principal principal, UserUpdateRequest userUpdateRequest);

    List<UserResponse> getAll(Pageable pageable);

    UserResponse getById(Long userId);

    UserResponse getByUsername(String username);

    void deleteById(Long userId, Principal principal);
}
