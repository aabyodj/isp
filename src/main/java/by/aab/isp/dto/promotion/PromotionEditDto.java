package by.aab.isp.dto.promotion;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionEditDto {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 25)
    private String name;

    @NotBlank
    @Size(min = 2, max = 100)
    private String description;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate activeSince;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate activeUntil;
}
