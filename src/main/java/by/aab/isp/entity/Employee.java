package by.aab.isp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Employee extends User {

    private Role role;

    public enum Role {
        ADMIN, MANAGER
    }

}
