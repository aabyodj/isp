package by.aab.isp.service.converter.user;

import static by.aab.isp.Const.LDT_FOR_AGES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.SubscriptionRepository;
import by.aab.isp.repository.entity.Customer;
import by.aab.isp.service.Now;
import by.aab.isp.service.converter.subscription.SubscriptionToSubscriptionViewDtoConverter;
import by.aab.isp.service.dto.user.CustomerViewDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerToCustomerViewDtoConverter implements Converter<Customer, CustomerViewDto> {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionToSubscriptionViewDtoConverter subscriptionConverter;

    @Autowired
    private Now now;

    @Override
    public CustomerViewDto convert(Customer entity) {
        CustomerViewDto dto = CustomerViewDto.builder()
                .balance(entity.getBalance())
                .permittedOverdraft(entity.getPermittedOverdraft())
                .payoffDate(entity.getPayoffDate().isBefore(LDT_FOR_AGES) ? entity.getPayoffDate() : null)
                .activeSubscriptions(subscriptionRepository.findByCustomerIdAndActivePeriodContains(entity.getId(), now.getLocalDateTime())
                        .stream()
                        .map(subscriptionConverter::convert)
                        .toList())
                .build();
        UserToUserViewDtoConverter.setFields(entity, dto);
        return dto;
    }

}
