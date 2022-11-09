package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.dto.user.UserEditDto;
import by.aab.isp.repository.entity.Customer;
import by.aab.isp.repository.entity.Employee;
import by.aab.isp.repository.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserToUserEditDtoConverter implements Converter<User, UserEditDto> {

    private final CustomerToCustomerEditDtoConverter customerConverter;
    private final EmployeeToEmployeeEditDtoConverter employeeConverter;

    @Override
    public UserEditDto convert(User user) {
        if (user instanceof Customer customer) {
            return customerConverter.convert(customer);
        } else if (user instanceof Employee employee) {
            return employeeConverter.convert(employee);
        } else {
            throw new IllegalArgumentException("No converter for entity " + user.getClass());
        }
    }

    static void setFields(User entity, UserEditDto dto) {
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
    }

}
