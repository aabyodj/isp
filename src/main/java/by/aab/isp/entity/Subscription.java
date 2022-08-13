package by.aab.isp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Subscription implements Entity {

    private long id;
    private Customer customer;
    private Tariff tariff;
    private BigDecimal price;
    private Instant activeSince;
    private Instant activeUntil;
}
