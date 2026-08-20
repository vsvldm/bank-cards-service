package com.bank.cards.domain.port;

import com.bank.cards.domain.valueobject.EncryptedData;

public interface EncryptionPort {

    EncryptedData encrypt(String plainText);

    String decrypt(EncryptedData encryptedData);
}
