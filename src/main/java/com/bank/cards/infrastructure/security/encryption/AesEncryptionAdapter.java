package com.bank.cards.infrastructure.security.encryption;

import com.bank.cards.domain.port.EncryptionPort;
import com.bank.cards.domain.valueobject.EncryptedData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesEncryptionAdapter implements EncryptionPort {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom;

    public AesEncryptionAdapter(@Value("${encryption.key}") String key) {
        this.secretKey = new SecretKeySpec(key.getBytes(), "AES");
        this.secureRandom = new SecureRandom();
    }

    @Override
    public EncryptedData encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes());

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return new EncryptedData(Base64.getEncoder().encodeToString(combined));

        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt data", e);
        }
    }

    @Override
    public String decrypt(EncryptedData encryptedData) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData.value());

            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);

            byte[] cipherText = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainText = cipher.doFinal(cipherText);

            return new String(plainText);

        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt data", e);
        }
    }
}