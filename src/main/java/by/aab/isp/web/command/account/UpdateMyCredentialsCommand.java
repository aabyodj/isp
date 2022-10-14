package by.aab.isp.web.command.account;

import by.aab.isp.dto.UpdateCredentialsDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class UpdateMyCredentialsCommand extends Command {
    private final UserService userService;

    public UpdateMyCredentialsCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        UserDto user = (UserDto) req.getAttribute("activeUser");
        UpdateCredentialsDto dto = new UpdateCredentialsDto();
        dto.setUserId(user.getId());
        dto.setEmail(req.getParameter("email"));
        String newPassword = req.getParameter("new-password1");
        if (!Objects.equals(newPassword, req.getParameter("new-password2"))) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented");  //TODO: implement this
        }
        if (null != newPassword && newPassword.isBlank()) {
            newPassword = null;
        }
        dto.setNewPassword(newPassword);
        dto.setCurrentPassword(req.getParameter("current-password"));
        userService.updateCredentials(dto);
        String redirect = req.getParameter("redirect");
        req.getSession().invalidate();
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user != null;
    }
}
