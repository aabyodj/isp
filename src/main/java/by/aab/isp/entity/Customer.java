package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=true)
public class Customer extends User {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private Instant payoffDate;

}
