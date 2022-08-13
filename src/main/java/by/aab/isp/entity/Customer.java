package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=true)
public class Customer extends User {

    private Tariff tariff;  //TODO: Eliminate this. Use Subscription entity instead
    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private Instant payoffDate;

}
