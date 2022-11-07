package by.aab.isp.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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

    @NotNull
    private BigDecimal balance;

    @NotNull
    @PositiveOrZero
    private BigDecimal permittedOverdraft;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate payoffDate;

}
