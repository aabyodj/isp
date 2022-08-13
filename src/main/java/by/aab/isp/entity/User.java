package by.aab.isp.entity;

import lombok.Data;

@Data
public abstract class User implements Entity {

    private long id;
    private String email;

}
