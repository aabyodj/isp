package by.aab.isp.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Tariff implements Entity {

    private Long id;
    private String name;
    private String description;

    /**
     * Kb/s
     * null = unlimited
     */
    private Integer bandwidth;

    /**
     * Bytes
     * null = unlimited
     */
    private Long includedTraffic;

    private BigDecimal price;
    private boolean active;
}
