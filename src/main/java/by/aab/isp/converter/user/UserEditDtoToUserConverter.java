package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.repository.entity.User;
import by.aab.isp.service.dto.user.CustomerEditDto;
import by.aab.isp.service.dto.user.EmployeeEditDto;
import by.aab.isp.service.dto.user.UserEditDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserEditDtoToUserConverter implements Converter<UserEditDto, User> {

    private final CustomerEditDtoToCustomerConverter customerConverter;
    private final EmployeeEditDtoToEmployeeConverter employeeConverter;

    @Override
    public User convert(UserEditDto user) {
        if (user instanceof CustomerEditDto customer) {
            return customerConverter.convert(customer);
        } else if (user instanceof EmployeeEditDto employee) {
            return employeeConverter.convert(employee);
        } else {
            throw new IllegalArgumentException("No converter for DTO " + user.getClass());
        }
    }

}
