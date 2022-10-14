package by.aab.isp.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class UpdateCredentialsDto {
    private long userId;

    @ToString.Exclude
    private String email;

    @ToString.Exclude
    private String currentPassword;

    @ToString.Exclude
    private String newPassword;
}
