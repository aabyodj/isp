package by.aab.isp.converter.user;

import static by.aab.isp.Const.LDT_FOR_AGES;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.repository.entity.Customer;
import by.aab.isp.service.dto.user.CustomerEditDto;

@Service
public class CustomerToCustomerEditDtoConverter implements Converter<Customer, CustomerEditDto> {

    @Override
    public CustomerEditDto convert(Customer entity) {
        CustomerEditDto dto = CustomerEditDto.builder()
                .balance(entity.getBalance())
                .permittedOverdraft(entity.getPermittedOverdraft())
                .payoffDate(entity.getPayoffDate().isBefore(LDT_FOR_AGES) ? entity.getPayoffDate().toLocalDate()
                                                                          : null)
                .build();
        UserToUserEditDtoConverter.setFields(entity, dto);
        return dto;
    }

}
