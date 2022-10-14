package by.aab.isp.dto;

import by.aab.isp.entity.Employee.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class EmployeeDto extends UserDto {

    private Role role;

}
