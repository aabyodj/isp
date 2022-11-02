package by.aab.isp.dto.tariff;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffEditDto {

    private Long id;

    @NotBlank
    @Size(min=2, max=15)
    private String name;

    @NotBlank
    @Size(min=2, max=50)
    private String description;

    /**
     * Kb/s
     */
    @PositiveOrZero
    private Integer bandwidth;

    /**
     * Megabytes
     */
    @PositiveOrZero
    private Long includedTraffic;

    @Positive
    private BigDecimal price;

    private boolean active;
}
