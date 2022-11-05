package by.aab.isp.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class CustomerEditDto extends UserEditDto {

    private BigDecimal balance;
    private BigDecimal permittedOverdraft;
    private LocalDateTime payoffDate;

}
