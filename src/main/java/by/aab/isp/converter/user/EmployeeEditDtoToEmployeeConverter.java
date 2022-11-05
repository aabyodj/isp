package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.dto.user.EmployeeEditDto;
import by.aab.isp.entity.Employee;
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
