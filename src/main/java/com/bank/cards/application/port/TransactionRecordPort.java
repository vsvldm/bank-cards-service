package com.bank.cards.application.port;

import com.bank.cards.domain.valueobject.CardId;
import java.math.BigDecimal;

public interface TransactionRecordPort {
    void recordTransfer(CardId fromCardId, CardId toCardId, BigDecimal amount);
}