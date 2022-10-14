package by.aab.isp.dto;

import lombok.Data;

@Data
public abstract class UserDto {

    private Long id;
    private String email;
    private String password;
    private boolean active;

}
