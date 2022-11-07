package by.aab.isp.validator;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import by.aab.isp.dto.user.UpdateCredentialsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCredentialsDtoValidator implements Validator {

    private final EmailAvailabilityValidator emailValidator;
    private final PasswordStrengthValidator passwordValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateCredentialsDto.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UpdateCredentialsDto credentials = (UpdateCredentialsDto) object;
        if (!emailValidator.isEmailAvailable(credentials.getEmail(), credentials.getUserId())) {
            errors.rejectValue("email", "msg.validation.email.busy");
        };
        String password = credentials.getNewPassword();
        if (password != null && !password.isBlank() && !passwordValidator.isStrongPassword(password)) {
            errors.rejectValue("newPassword", "msg.validation.password.too-weak");
        }
        if (!Objects.equals(credentials.getNewPassword(), credentials.getNewPasswordConfirmation())) {
            errors.rejectValue("newPassword", "msg.validation.passwords-dont-match");
            errors.rejectValue("newPasswordConfirmation", "msg.validation.passwords-dont-match");
        }
    }

}
