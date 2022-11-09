package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.repository.entity.Employee;

@Component
public class EmployeeToEmployeeViewDtoConverter implements Converter<Employee, EmployeeViewDto> {

    @Override
    public EmployeeViewDto convert(Employee entity) {
        EmployeeViewDto dto = EmployeeViewDto.builder()
                .role(entity.getRole())
                .build();
        UserToUserViewDtoConverter.setFields(entity, dto);
        return dto;
    }

}
