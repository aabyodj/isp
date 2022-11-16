package by.aab.isp.service.dto.promotion;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionViewDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime activeSince;
    private LocalDateTime activeUntil;
    private boolean active;

}
