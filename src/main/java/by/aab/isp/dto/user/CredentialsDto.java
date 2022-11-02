package by.aab.isp.dto.user;

import lombok.Data;
import lombok.ToString;

@Data
public class CredentialsDto {

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
}
