package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Employee;
import by.aab.isp.service.dto.user.EmployeeViewDto;

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
