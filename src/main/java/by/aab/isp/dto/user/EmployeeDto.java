package by.aab.isp.dto.user;

import by.aab.isp.entity.Employee.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class EmployeeDto extends UserEditDto {

    private Role role;

}
