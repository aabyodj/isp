package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "permitted_overdraft", nullable = false)
    private BigDecimal permittedOverdraft;

    @Column(name = "payoff_date", nullable = false)
    private LocalDateTime payoffDate;

}
