package by.aab.isp.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class CredentialsDto {

    @ToString.Exclude
    private String email;

    @ToString.Exclude
    private String password;
}
