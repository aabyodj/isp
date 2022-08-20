package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class Customer extends User {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private LocalDateTime payoffDate;

}
