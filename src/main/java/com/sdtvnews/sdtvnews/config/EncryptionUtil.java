package com.sdtvnews.sdtvnews.config;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // 16-byte key for AES-128

    // Encrypt the data
    public String encrypt(String data) {
        try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            // You could also return null or an empty string, or throw a runtime exception
            System.err.println("Encryption error: " + e.getMessage());
            return null; // Or throw new RuntimeException("Encryption failed", e);
        }
    }

    // Decrypt the data
    public String decrypt(String encryptedData) {
        try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] originalData = cipher.doFinal(decodedData);
            return new String(originalData);
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            System.err.println("Decryption error: " + e.getMessage());
            return null; // Or throw new RuntimeException("Decryption failed", e);
        }
    }
}
