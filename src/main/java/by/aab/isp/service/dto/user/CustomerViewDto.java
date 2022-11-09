package by.aab.isp.service.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import by.aab.isp.service.dto.subscription.SubscriptionViewDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerViewDto extends UserViewDto {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private LocalDateTime payoffDate;
    private List<SubscriptionViewDto> activeSubscriptions;

}
