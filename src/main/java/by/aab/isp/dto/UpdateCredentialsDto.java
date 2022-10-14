package by.aab.isp.dto;

import lombok.Data;

@Data
public class UpdateCredentialsDto {
    private long userId;
    private String email;
    private String currentPassword;
    private String newPassword;
}
