package by.aab.isp.entity;

import lombok.Data;

@Data
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
