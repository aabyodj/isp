package by.aab.isp.entity;

import lombok.Data;

@Data
public abstract class User implements Entity {

    private Long id;
    private String email;
    private byte[] passwordHash;
    private boolean active;

}
