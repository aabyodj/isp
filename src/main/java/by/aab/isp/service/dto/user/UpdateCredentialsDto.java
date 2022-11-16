package by.aab.isp.service.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
public class UpdateCredentialsDto {
    private long userId;

    @NotNull(message = "{msg.validation.not-blank}")
    @Email(regexp = ".{3,50}", message = "{msg.validation.email}")
    private String email;

    @ToString.Include(name = "email")
    private String getHiddenEmail() {
        return "[PROTECTED]";
    }

    private String currentPassword;

    @ToString.Include(name = "currentPassword")
    private String getHiddenCurrentPassword() {
        return "[PROTECTED]";
    }

    private String newPassword;

    @ToString.Include(name = "newPassword")
    private String getHiddenNewPassword() {
        return "[PROTECTED]";
    }

    private String newPasswordConfirmation;

    @ToString.Include(name = "newPasswordConfirmation")
    private String getHiddenNewPasswordConfirmation() {
        return "[PROTECTED]";
    }
}
