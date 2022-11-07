package by.aab.isp.dto.tariff;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "{msg.validation.not-blank}")
    @Size(min=2, max=15, message = "{msg.validation.size}")
    private String name;

    @NotBlank(message = "{msg.validation.not-blank}")
    @Size(min=2, max=50, message = "{msg.validation.size}")
    private String description;

    /**
     * Kb/s
     */
    @NotNull(message = "{msg.validation.not-blank}")
    @PositiveOrZero(message = "{msg.validation.positive-or-zero}")
    private Integer bandwidth;

    /**
     * Megabytes
     */
    @NotNull(message = "{msg.validation.not-blank}")
    @PositiveOrZero(message = "{msg.validation.positive-or-zero}")
    private Long includedTraffic;

    @NotNull(message = "{msg.validation.not-blank}")
    @Positive(message = "{msg.validation.positive}")
    private BigDecimal price;

    private boolean active;
}
