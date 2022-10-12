package by.aab.isp.repository;

import by.aab.isp.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee> {

    long countByRoleAndActive(Employee.Role role, boolean active);

    long countByNotIdAndRoleAndActive(long id, Employee.Role role, boolean active);

}
