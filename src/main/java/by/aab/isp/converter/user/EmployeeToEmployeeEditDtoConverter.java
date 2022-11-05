package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.dto.user.EmployeeEditDto;
import by.aab.isp.entity.Employee;

@Service
public class EmployeeToEmployeeEditDtoConverter implements Converter<Employee, EmployeeEditDto> {

    @Override
    public EmployeeEditDto convert(Employee entity) {
        EmployeeEditDto dto = EmployeeEditDto.builder()
                .role(entity.getRole())
                .build();
        UserToUserEditDtoConverter.setFields(entity, dto);
        return dto;
    }

}
