package by.aab.isp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "traffic_consumed", nullable = false)
    private long trafficConsumed;

    @Column(name = "traffic_per_period")
    private Long trafficPerPeriod;

    @Column(name = "active_since")
    private LocalDateTime activeSince;

    @Column(name = "active_until")
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
