package by.aab.isp.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Tariff implements Entity {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    
}
