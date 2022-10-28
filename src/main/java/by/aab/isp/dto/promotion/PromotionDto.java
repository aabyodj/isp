package by.aab.isp.dto.promotion;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PromotionDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime activeSince;
    private LocalDateTime activeUntil;
    private boolean active;
}
