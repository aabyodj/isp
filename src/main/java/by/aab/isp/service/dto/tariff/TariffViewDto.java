package by.aab.isp.service.dto.tariff;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TariffViewDto {
    private long id;
    private String name;
    private String description;
    private String bandwidth;
    private String includedTraffic;
    private String price;
    private boolean active;
}
