package by.aab.isp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Data
public class Subscription implements Entity {

    private long id;
    private Customer customer;
    private Tariff tariff;
    private BigDecimal price;
    private long trafficConsumed;
    private Long trafficPerPeriod;
    private LocalDateTime activeSince;
    private LocalDateTime activeUntil;

    public Long getTrafficLeft() {
        return trafficPerPeriod != null ? trafficPerPeriod - trafficConsumed
                                        : null;
    }

    public boolean isActiveOn(LocalDateTime instant) {
        return !(activeSince != null && activeSince.isAfter(instant)
                || activeUntil != null && activeUntil.isBefore(instant));
    }

    public static final Comparator<Subscription> SORT_BY_ACTIVE_SINCE = (s1, s2) -> {
        if (s1.getActiveSince() != null && s2.getActiveSince() != null) {
            return s1.getActiveSince().compareTo(s2.getActiveSince());
        }
        if (s1.getActiveSince() != null) {
            return 1;
        }
        if (s2.getActiveSince() != null) {
            return -1;
        }
        return 0;
    };

    public static final Comparator<Subscription> SORT_BY_ACTIVE_UNTIL = (s1, s2) -> {
        if (s1.getActiveUntil() != null && s2.getActiveUntil() != null) {
            return s1.getActiveUntil().compareTo(s2.getActiveUntil());
        }
        if (s1.getActiveUntil() != null) {
            return -1;
        }
        if (s2.getActiveUntil() != null) {
            return 1;
        }
        return 0;
    };
}
