package by.aab.isp.repository.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tariffs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

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
     */
    @Column(name = "bandwidth", nullable = false)
    private int bandwidth;

    /**
     * Bytes
     */
    @Column(name = "included_traffic", nullable = false)
    private long includedTraffic;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "active", nullable = false)
    private boolean active;

}
