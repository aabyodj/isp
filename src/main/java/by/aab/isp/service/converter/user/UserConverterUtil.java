package by.aab.isp.service.converter.user;

import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.User;
import by.aab.isp.service.converter.PasswordUtil;
import by.aab.isp.service.dto.user.UserEditDto;
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
