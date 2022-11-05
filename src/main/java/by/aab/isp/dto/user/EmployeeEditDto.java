package by.aab.isp.dto.user;

import javax.validation.constraints.NotNull;

import by.aab.isp.entity.Employee.Role;
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

    @NotNull
    private Role role;

}
