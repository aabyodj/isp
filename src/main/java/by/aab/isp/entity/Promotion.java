package by.aab.isp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Promotion implements Entity {

    public Promotion() {
        id = 0;
        name = "";
        description = "";
    }

    private long id;
    private String name;
    private String description;

}
