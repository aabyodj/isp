package by.aab.isp.entity;

import lombok.Data;

@Data
public class User implements Entity {

    private long id;
    private String email;
    private Role role;

    public enum Role {
        ADMIN, MANAGER, CUSTOMER
    }

}
