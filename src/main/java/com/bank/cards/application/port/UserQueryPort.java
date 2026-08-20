package com.bank.cards.application.port;

import com.bank.cards.domain.valueobject.UserId;

public interface UserQueryPort {
    UserId getUserIdByUsername(String username);
}