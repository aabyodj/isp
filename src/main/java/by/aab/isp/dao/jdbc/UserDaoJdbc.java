package by.aab.isp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

@Repository("userDao")
public final class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {

    private static final String USERS_TABLE_NAME = "users";
    private static final String CUSTOMERS_TABLE_NAME = "customers";
    private static final String[] CUSTOMER_FIELDS = {
            "user_id", "balance", "permitted_overdraft", "payoff_date"};
    private static final String EMPLOYEES_TABLE_NAME = "employees";
    private static final String[] EMPLOYEE_FIELDS = {"user_id", "role_id"};
    private static final Map<String, String> FIELD_NAMES_MAP = Map.of(
            "email", "email",
            "active", "active",
            "balance", "balance",
            "permittedOverdraft", "permitted_overdraft",
            "payoffDate", "payoff_date",
            "role", "role_id"
    );

    private final String customersQuotedTableName = quote(CUSTOMERS_TABLE_NAME);
    private final String employeesQuotedTableName = quote(EMPLOYEES_TABLE_NAME);

    private final String sqlInsertCustomer = "INSERT INTO " + customersQuotedTableName
            + Arrays.stream(CUSTOMER_FIELDS)
                    .map(this::quote)
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge)
            + " VALUES "
            + Arrays.stream(CUSTOMER_FIELDS)
                    .map(field -> ":" + field)
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge);

    private final String sqlInsertEmployee = "INSERT INTO " + employeesQuotedTableName
            + Arrays.stream(EMPLOYEE_FIELDS)
                    .map(this::quote)
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge)
            + " VALUES "
            + Arrays.stream(EMPLOYEE_FIELDS)
                    .map(field -> ":" + field)
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge);

    private final String sqlSelectJoinCustomers = "SELECT * FROM " + quotedTableName
            + " JOIN " + customersQuotedTableName
            + " ON " + quote("id") + "=" + quote("user_id");
    private final String sqlSelectJoinCustomerWhereId = sqlSelectJoinCustomers
            + " WHERE " + quote("id") + "=";
    private final String sqlSelectJoinCustomerWhereEmailAndActive = sqlSelectJoinCustomers
            + " WHERE " + quote("email") + " = :email AND " + quote("active") + " = :active";
    private final String sqlSelectJoinEmployees = "SELECT * FROM " + quotedTableName
            + " JOIN " + employeesQuotedTableName
            + " ON " + quote("id") + "=" + quote("user_id");
    private final String sqlSelectJoinEmployeeWhereId = sqlSelectJoinEmployees
            + " WHERE " + quote("id") + "=";
    private final String sqlSelectJoinEmployeeWhereEmailAndActive = sqlSelectJoinEmployees
            + " WHERE " + quote("email") + " = :email AND " + quote("active") +  "= :active";
    private final String sqlSelectCustomersWhereId = "SELECT * FROM " + customersQuotedTableName
            + " WHERE " + quote("user_id") + "=";
    private final String sqlSelectEmployeesWhereId = "SELECT * FROM " + employeesQuotedTableName
            + " WHERE " + quote("user_id") + "=";
    private final String sqlCountCustomers = "SELECT count(*) FROM " + customersQuotedTableName;
    private final String sqlCountEmployees = "SELECT count(*) FROM " + employeesQuotedTableName;
    private final String sqlCountJoinEmployees = "SELECT count(*) FROM " + quotedTableName
            + " JOIN " + employeesQuotedTableName
            + " ON " + quote("id") + "=" + quote("user_id");
    private final String sqlUpdateUserWithoutHash = "UPDATE " + quotedTableName
            + " SET " + quote("email") + " = :email, " + quote("active") + " = :active"
            + " WHERE " + quote("id") + "=";
    private final String sqlUpdateCustomer = "UPDATE " + customersQuotedTableName
            + " SET " + quote("balance") + " = :balance, "
            + quote("permitted_overdraft") + " = :permitted_overdraft, " + quote("payoff_date") + " = :payoff_date"
            + " WHERE " + quote("user_id") + "=";
    private final String sqlUpdateEmployee = "UPDATE " + employeesQuotedTableName
            + " SET " + quote("role_id") + " = :role_id"
            + " WHERE " + quote("user_id") + "=";

    public UserDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, USERS_TABLE_NAME, List.of("email", "password_hash", "active"));
    }

    @Override
    Map<String, ?> entityToMap(User user) {
        return Map.of(
                "email", user.getEmail(),
                "password_hash", user.getPasswordHash(),
                "active", user.isActive());
    }

    void mapUserToRowWithoutHash(User user, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, user.getEmail());
            row.setBoolean(++c, user.isActive());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Map<String, ?> customerToMap(Customer customer) {
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", customer.getId());
        result.put("balance", customer.getBalance());
        result.put("permitted_overdraft", customer.getPermittedOverdraft());
        result.put("payoff_date", customer.getPayoffDate());
        return result;
    }

    private Map<String, ?> employeeToMap(Employee employee) {
        return Map.of(
                "user_id", employee.getId(),
                "role_id", employee.getRole().ordinal());
    }

    /**
     * <b>This method is implemented for compatibility only. You should use either mapRowToCustomer() or mapRowToEmployee().</b>
     * <p>Map one row from ResultSet to a User. As soon as User is abstract, this method will perform up to 2 additional
     * queries to determine what particular class should it return.
     * @param row ResultSet of a SQL query. This method will NOT call next() on it.
     * @return Either Customer or Employee
     */
    @Override
    User mapRowToObject(ResultSet row) {
        try {
            long id = row.getLong("id");
            String email = row.getString("email");
            byte[] password_hash = row.getBytes("password_hash");
            boolean active = row.getBoolean("active");
            User user = findOne(sqlSelectCustomersWhereId + id, this::mapRowToCustomer).orElse(null);
            if (null == user) {
                user = findOne(sqlSelectEmployeesWhereId + id, this::mapRowToEmployee).orElse(null);
            }
            if (null == user) {
                throw new DaoException("Inconsistent database: could not find neither Customer nor Employee for userId=" + id);
            }
            user.setId(id);
            user.setEmail(email);
            user.setPasswordHash(password_hash);
            user.setActive(active);
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void mapRowToUser(ResultSet row, User user) {
        try {
            user.setId(row.getLong("id"));
            user.setEmail(row.getString("email"));
            user.setPasswordHash(row.getBytes("password_hash"));
            user.setActive(row.getBoolean("active"));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Customer mapRowToCustomer(ResultSet row) {
        try {
            Customer customer = new Customer();
            if (row.getMetaData().getColumnCount() > CUSTOMER_FIELDS.length) {
                mapRowToUser(row, customer);
            }
            customer.setBalance(row.getBigDecimal("balance"));
            customer.setPermittedOverdraft(row.getBigDecimal("permitted_overdraft"));
            Timestamp payoffDate = row.getTimestamp("payoff_date");
            customer.setPayoffDate(payoffDate != null ? payoffDate.toLocalDateTime()
                                                      : null);
            return customer;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Employee mapRowToEmployee(ResultSet row) {
        try {
            Employee employee = new Employee();
            if (row.getMetaData().getColumnCount() > EMPLOYEE_FIELDS.length) {
                mapRowToUser(row, employee);
            }
            employee.setRole(Employee.Role.values()[row.getInt("role_id")]);
            return employee;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Iterable<Customer> mapRowsToCustomers(ResultSet rows) {
        try {
            Collection<Customer> result = new LinkedList<>();
            while (rows.next()) {
                try {
                    result.add(mapRowToCustomer(rows));
                } catch (DaoException ignore) {}
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Iterable<Employee> mapRowsToEmployees(ResultSet rows) {
        try {
            Collection<Employee> result = new LinkedList<>();
            while (rows.next()) {
                try {
                    result.add(mapRowToEmployee(rows));
                } catch (DaoException ignore) {}
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    User objectWithId(User user, Long id) {
        user.setId(id);
        return user;
    }

    @Override
    public User save(User user) {
        user = super.save(user);
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            jdbcTemplate.update(sqlInsertCustomer, customerToMap(customer));
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            jdbcTemplate.update(sqlInsertEmployee, employeeToMap(employee));
        } else {
            //FIXME: remove row from "users" table
            throw new RuntimeException("Unimplemented for " + user.getClass());
        }
        return user;
    }

    @Override
    public Iterable<Customer> findAllCustomers(OrderOffsetLimit orderOffsetLimit) {
        return jdbcTemplate.query(
                sqlSelectJoinCustomers + formatOrderOffsetLimit(orderOffsetLimit),
                this::mapRowsToCustomers);
    }

    @Override
    public Iterable<Employee> findAllEmployees(OrderOffsetLimit orderOffsetLimit) {
        return jdbcTemplate.query(
                sqlSelectJoinEmployees + formatOrderOffsetLimit(orderOffsetLimit),
                this::mapRowsToEmployees);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Customer> findCustomerById(long id) {
        return (Optional<Customer>) findOne(sqlSelectJoinCustomerWhereId + id, this::mapRowToCustomer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Employee> findEmployeeById(long id) {
        return (Optional<Employee>) findOne(sqlSelectJoinEmployeeWhereId + id, this::mapRowToEmployee);
    }

    @SuppressWarnings("unchecked")
    Optional<Customer> findCustomerByEmailAndActive(String email, boolean active) {
        return (Optional<Customer>) findOne(
                sqlSelectJoinCustomerWhereEmailAndActive,
                Map.of(
                        "email", email,
                        "active", active),
                this::mapRowToCustomer);
    }

    @SuppressWarnings("unchecked")
    Optional<Employee> findEmployeeByEmailAndActive(String email, boolean active) {
        return (Optional<Employee>) findOne(
                sqlSelectJoinEmployeeWhereEmailAndActive,
                Map.of(
                        "email", email,
                        "active", active),
                this::mapRowToEmployee);
    }

    @Override
    public Optional<User> findByEmailAndActive(String email, boolean active) {
        return Optional.ofNullable(
                findCustomerByEmailAndActive(email, active)
                .map(customer -> (User) customer)
                .orElse(findEmployeeByEmailAndActive(email, active).orElse(null)));
    }

    @Override
    public long countCustomers() {
        return count(sqlCountCustomers);
    }

    @Override
    public long countEmployees() {
        return count(sqlCountEmployees);
    }

    @Override
    public long countByRoleAndActive(Employee.Role role, boolean active) {
        return count(sqlCountJoinEmployees
                + " WHERE " + quote("role_id") + "=" + role.ordinal()
                + " AND " + quote("active") + "=" + active);
    }

    @Override
    public long countByNotIdAndRoleAndActive(long id, Employee.Role role, boolean active) {
        return count(sqlCountJoinEmployees
                + " WHERE " + quote("id") + "!=" + id
                + " AND " + quote("role_id") + "=" + role.ordinal()
                + " AND " + quote("active") + "=" + active);
    }

    @Override
    public void update(User user) {
        if (user.getPasswordHash() != null) {
            super.update(user);
        } else {
            int result = jdbcTemplate.update(
                    sqlUpdateUserWithoutHash + user.getId(),
                    Map.of(
                            "email", user.getEmail(),
                            "active", user.isActive()));
            if (result < 1) {
                throw new DaoException("Could not update user");
            }
        }
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            int result = jdbcTemplate.update(sqlUpdateCustomer + user.getId(), customerToMap(customer));
            if (result < 1) {
                throw new DaoException("Could not update customer");
            }
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            int result = jdbcTemplate.update(sqlUpdateEmployee + user.getId(), employeeToMap(employee));
            if (result < 1) {
                throw new DaoException("Could not update employee");
            }
        } else {
            throw new DaoException("Unimplemented for " + user.getClass());
        }
    }

    @Override
    String mapFieldName(String fieldName) {
        return FIELD_NAMES_MAP.get(fieldName);
    }

    @Override
    String mapNullsOrder(OrderOffsetLimit.Order order) {
        if ("payoffDate".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }
}
