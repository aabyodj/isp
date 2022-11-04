package by.aab.isp.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class CustomerEditDto extends UserEditDto {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private LocalDateTime payoffDate;

}
