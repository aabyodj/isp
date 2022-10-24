package by.aab.isp.dto.subscription;

import java.math.BigDecimal;

import by.aab.isp.dto.tariff.ShowTariffDto;
import lombok.Data;

@Data
public class SubscriptionDto {
    private long id;
    private ShowTariffDto tariff;
    private BigDecimal price;
    private String trafficConsumed;
    private String trafficLeft;
    private String trafficPerPeriod;
    private String activeSince;
    private String activeUntil;
    private boolean active;
}
