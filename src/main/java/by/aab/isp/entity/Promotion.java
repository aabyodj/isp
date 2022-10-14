package by.aab.isp.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "active_since")
    private LocalDateTime activeSince;

    @Column(name = "active_until")
    private LocalDateTime activeUntil;

    public boolean isActiveOn(LocalDateTime instant) {
        return !(activeSince != null && activeSince.isAfter(instant)
                || activeUntil != null && activeUntil.isBefore(instant));
    }

    public static final Comparator<Promotion> SORT_BY_ACTIVE_SINCE = (p1, p2) -> {
        if (p1.getActiveSince() != null && p2.getActiveSince() != null) {
            return p1.getActiveSince().compareTo(p2.getActiveSince());
        }
        if (p1.getActiveSince() != null) {
            return 1;
        }
        if (p2.getActiveSince() != null) {
            return -1;
        }
        return 0;
    };

    public static final Comparator<Promotion> SORT_BY_ACTIVE_UNTIL = (p1, p2) -> {
        if (p1.getActiveUntil() != null && p2.getActiveUntil() != null) {
            return p1.getActiveUntil().compareTo(p2.getActiveUntil());
        }
        if (p1.getActiveUntil() != null) {
            return -1;
        }
        if (p2.getActiveUntil() != null) {
            return 1;
        }
        return 0;
    };

}
