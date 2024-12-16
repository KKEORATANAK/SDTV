package com.sdtvnews.sdtvnews.config;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionImageUtil {

    // Method to hash a string using SHA-256
    public static String hash64(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();  // SHA-256 produces a 64-character hex string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing input", e);
        }
    }
}
