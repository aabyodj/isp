package by.aab.isp.entity;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tariff {
    
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    
}
