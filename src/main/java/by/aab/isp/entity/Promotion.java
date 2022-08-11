package by.aab.isp.entity;

import lombok.Data;

@Data
public class Promotion implements Entity {

    private long id;
    private String name;
    private String description;

}
