package by.aab.isp.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@javax.persistence.Entity
@Table(name = "tariff")
public class Tariff implements Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    /**
     * Kb/s
     * null = unlimited
     */
    @Column(name = "bandwidth")
    private Integer bandwidth;

    /**
     * Bytes
     * null = unlimited
     */
    @Column(name = "included_traffic")
    private Long includedTraffic;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "active", nullable = false)
    private boolean active;

}
