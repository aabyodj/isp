package by.aab.isp.service.validator;

import org.springframework.stereotype.Service;

@Service
public class PasswordStrengthValidator {

    public boolean isStrongPassword(String password) {
        return password.length() > 1;
    }

}
