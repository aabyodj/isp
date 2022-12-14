package by.aab.isp.service.converter.user;

import static by.aab.isp.Const.LDT_FOR_AGES;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import by.aab.isp.repository.entity.Customer;
import by.aab.isp.service.dto.user.CustomerEditDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerEditDtoToCustomerConverter implements Converter<CustomerEditDto, Customer> {

    private final UserConverterUtil util;

    @Override
    public Customer convert(CustomerEditDto dto) {
        Customer entity = Customer.builder()
                .balance(dto.getBalance())
                .permittedOverdraft(dto.getPermittedOverdraft())
                .payoffDate(dto.getPayoffDate() != null ? dto.getPayoffDate().plusDays(1).atStartOfDay().minusNanos(1000)
                                                        : LDT_FOR_AGES)
                .build();
        util.setFields(dto, entity);
        return entity;
    }

}
