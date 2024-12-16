package com.sdtvnews.sdtvnews;

import com.sdtvnews.sdtvnews.config.EncryptionUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private final EncryptionUtil encryptionUtil = new EncryptionUtil();

    @Override
    public String encode(CharSequence rawPassword) {
        // You may not need this if you are not encoding during registration
        return encryptionUtil.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Decrypt the encoded password for comparison
        String decryptedPassword = encryptionUtil.decrypt(encodedPassword);
        return decryptedPassword != null && decryptedPassword.equals(rawPassword.toString());
    }
}
