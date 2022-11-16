package by.aab.isp.service.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class UserEditDto {

    private Long id;

    @NotNull
    @Email
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

    private String passwordConfirmation;

    @ToString.Include(name = "passwordConfirmation")
    private String getHiddenPasswordConfirmation() {
        return "[PROTECTED]";
    }

    private boolean active;

}
