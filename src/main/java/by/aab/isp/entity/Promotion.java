package by.aab.isp.entity;

import lombok.Data;

import java.time.Instant;
import java.util.Comparator;

@Data
public class Promotion implements Entity {

    private long id;
    private String name;
    private String description;
    private Instant activeSince;
    private Instant activeUntil;

    public boolean isActiveOn(Instant instant) {
        return !(activeSince != null && activeSince.isAfter(instant)
                || activeUntil != null && activeUntil.isBefore(instant));
    }

    public static final Comparator<Promotion> SORT_BY_ACTIVE_SINCE = (p1, p2) -> {
        if (p1.getActiveSince() != null && p2.getActiveSince() != null) {
            return p1.getActiveSince().compareTo(p2.getActiveSince());
        }
        if (p1.getActiveSince() != null) return 1;
        if (p2.getActiveSince() != null) return -1;
        return 0;
    };

    public static final Comparator<Promotion> SORT_BY_ACTIVE_UNTIL = (p1, p2) -> {
        if (p1.getActiveUntil() != null && p2.getActiveUntil() != null) {
            return p1.getActiveUntil().compareTo(p2.getActiveUntil());
        }
        if (p1.getActiveUntil() != null) return -1;
        if (p2.getActiveUntil() != null) return 1;
        return 0;
    };

}
