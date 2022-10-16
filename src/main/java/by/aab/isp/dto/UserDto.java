package by.aab.isp.dto;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class UserDto {

    private Long id;

    @ToString.Exclude
    private String email;

    @ToString.Exclude
    private String password;

    private boolean active;

}
