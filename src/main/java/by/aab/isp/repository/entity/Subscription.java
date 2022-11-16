package by.aab.isp.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "traffic_per_period", nullable = false)
    private long trafficPerPeriod;

    @Column(name = "active_since", nullable = false)
    private LocalDateTime activeSince;

    @Column(name = "active_until", nullable = false)
    private LocalDateTime activeUntil;
}
