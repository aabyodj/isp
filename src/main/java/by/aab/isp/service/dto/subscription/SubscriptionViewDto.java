package by.aab.isp.service.dto.subscription;

import java.math.BigDecimal;

import by.aab.isp.service.dto.tariff.TariffViewDto;
import lombok.Data;

@Data
public class SubscriptionViewDto {
    private long id;
    private TariffViewDto tariff;
    private BigDecimal price;
    private String trafficConsumed;
    private String trafficLeft;
    private String trafficPerPeriod;
    private String activeSince;
    private String activeUntil;
    private boolean active;
}
