package by.aab.isp.converter.user;

import org.springframework.stereotype.Component;

import by.aab.isp.converter.PasswordUtil;
import by.aab.isp.dto.user.UserEditDto;
import by.aab.isp.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserConverterUtil {
    
    private final PasswordUtil passwordUtil;

    void setFields(UserEditDto dto, User entity) {
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(passwordUtil.hashPassword(dto.getPassword()));
        entity.setActive(dto.isActive());
    }

}
