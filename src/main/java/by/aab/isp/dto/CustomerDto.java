package by.aab.isp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CustomerDto extends UserDto {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private LocalDateTime payoffDate;

}
