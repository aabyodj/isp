package by.aab.isp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User implements Entity {

    private long id;
    private String email;
    private Role role;

    public User() {
        email = "";
        role = Role.CUSTOMER;
    }

    public enum Role {
        ADMIN, MANAGER, CUSTOMER
    }

}
