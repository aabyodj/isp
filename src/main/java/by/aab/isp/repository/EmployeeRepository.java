package by.aab.isp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.aab.isp.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    long countByRoleAndActive(Employee.Role role, boolean active);

    @Query("SELECT count(*) FROM Employee e WHERE e.id != :id AND e.role = :role AND e.active = :active")
    long countByNotIdAndRoleAndActive(@Param("id") long id, @Param("role") Employee.Role role, @Param("active") boolean active);

}
