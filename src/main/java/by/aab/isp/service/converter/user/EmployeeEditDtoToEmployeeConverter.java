package by.aab.isp.service.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.repository.entity.Employee;
import by.aab.isp.service.dto.user.EmployeeEditDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeEditDtoToEmployeeConverter implements Converter<EmployeeEditDto, Employee> {

    private final UserConverterUtil util;

    @Override
    public Employee convert(EmployeeEditDto dto) {
        Employee entity = Employee.builder()
                .role(dto.getRole())
                .build();
        util.setFields(dto, entity);
        return entity;
    }

}
