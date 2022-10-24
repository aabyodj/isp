package by.aab.isp.dto.tariff;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TariffDto {
    private Long id;
    private String name;
    private String description;
    private Integer bandwidth;
    private Long includedTraffic;
    private BigDecimal price;
    private boolean active;
}
