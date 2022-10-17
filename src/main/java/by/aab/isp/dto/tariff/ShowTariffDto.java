package by.aab.isp.dto.tariff;

import lombok.Data;

@Data
public class ShowTariffDto {
    private long id;
    private String name;
    private String description;
    private String bandwidth;
    private String includedTraffic;
    private String price;
    private boolean active;
}
