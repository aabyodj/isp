package by.aab.isp.service.converter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class PasswordUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    public byte[] hashPassword(String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Empty password");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(password.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
