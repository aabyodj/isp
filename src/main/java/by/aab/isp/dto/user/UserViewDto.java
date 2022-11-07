package by.aab.isp.dto.user;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class UserViewDto {

    private long id;

    private String email;

    @ToString.Include(name = "email")
    private String getHiddenEmail() {
        return "[PROTECTED]";
    }

    private boolean active;

}
