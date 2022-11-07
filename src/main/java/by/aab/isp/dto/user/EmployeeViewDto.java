package by.aab.isp.dto.user;

import by.aab.isp.entity.Employee.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeViewDto extends UserViewDto {

    private Role role;
}
