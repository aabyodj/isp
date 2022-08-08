package by.aab.isp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class CustomerAccount implements Entity {

    private User user;
    private Tariff tariff;
    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private Instant payoffDate;

    public CustomerAccount(User user) {
        this.user = user;
        balance = BigDecimal.ZERO;
        permittedOverdraft = BigDecimal.ZERO;
    }

    @Override
    public long getId() {
        return user.getId();
    }
}
