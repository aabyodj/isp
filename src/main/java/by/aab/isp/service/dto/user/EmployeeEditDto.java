package by.aab.isp.service.dto.user;

import javax.validation.constraints.NotNull;

import by.aab.isp.repository.entity.Employee.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class EmployeeEditDto extends UserEditDto {

    @NotNull(message = "{msg.validation.not-blank}")
    private Role role;

}
