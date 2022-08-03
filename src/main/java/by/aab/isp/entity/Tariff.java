package by.aab.isp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Tariff implements Entity {

    public Tariff() {
        id = 0;
        name = "";
        description = "";
        price = BigDecimal.ZERO;
    }

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    
}
