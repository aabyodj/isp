package by.aab.isp.dto.user;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class UserDto {

    private Long id;

    private String email;

    @ToString.Include(name = "email")
    private String getHiddenEmail() {
        return "[PROTECTED]";
    }

    private String password;

    @ToString.Include(name = "password")
    private String getHiddenPassword() {
        return "[PROTECTED]";
    }

    private boolean active;

}
