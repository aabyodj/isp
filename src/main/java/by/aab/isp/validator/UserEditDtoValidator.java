package by.aab.isp.validator;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import by.aab.isp.service.dto.user.UserEditDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserEditDtoValidator implements Validator {

    private final EmailAvailabilityValidator emailValidator;
    private final PasswordStrengthValidator passwordValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEditDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEditDto user = (UserEditDto) target;
        if (!emailValidator.isEmailAvailable(user.getEmail(), user.getId())) {
            errors.rejectValue("email", "msg.validation.email.busy");
        }
        String password = user.getPassword();
        if (password != null && !password.isBlank() && !passwordValidator.isStrongPassword(password)) {
            errors.rejectValue("password", "msg.validation.password.too-weak");
        }
        if (!Objects.equals(user.getPassword(), user.getPasswordConfirmation())) {
            errors.rejectValue("password", "msg.validation.passwords-dont-match");
            errors.rejectValue("passwordConfirmation", "msg.validation.passwords-dont-match");
        }
    }

}
