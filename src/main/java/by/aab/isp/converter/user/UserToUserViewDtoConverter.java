package by.aab.isp.converter.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.repository.entity.Customer;
import by.aab.isp.repository.entity.Employee;
import by.aab.isp.repository.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserToUserViewDtoConverter implements Converter<User, UserViewDto> {

    private final CustomerToCustomerViewDtoConverter customerConverter;
    private final EmployeeToEmployeeViewDtoConverter employeeConverter;

    @Override
    public UserViewDto convert(User user) {
        if (user instanceof Customer customer) {
            return customerConverter.convert(customer);
        } else if (user instanceof Employee employee) {
            return employeeConverter.convert(employee);
        } else {
            throw new IllegalArgumentException("No converter for entity " + user.getClass());
        }
    }

    static void setFields(User entity, UserViewDto dto) {
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
    }

}
