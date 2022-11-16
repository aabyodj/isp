package by.aab.isp.service.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import by.aab.isp.service.dto.promotion.PromotionEditDto;

@Service
public class PromotionEditDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PromotionEditDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PromotionEditDto promotion = (PromotionEditDto) target;
        LocalDate activeSince = promotion.getActiveSince();
        LocalDate activeUntil = promotion.getActiveUntil();
        if (activeSince != null && activeUntil != null && activeSince.isAfter(activeUntil)) {
            errors.rejectValue("activeUntil", "msg.validation.date.must-start-from", new Object[]{activeSince}, null);
        }
;    }

}
