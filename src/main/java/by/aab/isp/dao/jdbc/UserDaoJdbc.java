package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public final class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {

    private static final String USERS_TABLE_NAME = "users";
    private static final String CUSTOMERS_TABLE_NAME = "customers";
    private static final String[] CUSTOMER_FIELDS = {
            "user_id", "balance", "permitted_overdraft", "payoff_date"};
    private static final String EMPLOYEES_TABLE_NAME = "employees";
    private static final String[] EMPLOYEE_FIELDS = {"user_id", "role_id"};

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
                    .map(field -> "?")
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
                    .map(field -> "?")
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge);

    private final String sqlSelectJoinCustomers = "SELECT * FROM " + quotedTableName
            + " JOIN " + customersQuotedTableName
            + " ON " + quote("id") + "=" + quote("user_id");
    private final String sqlSelectJoinCustomersOrderByEmail = sqlSelectJoinCustomers
            + " ORDER BY " + quote("email") + " ASC";
    private final String sqlSelectJoinCustomerWhereId = sqlSelectJoinCustomers
            + " WHERE " + quote("id") + "=";
    private final String sqlSelectJoinCustomerWhereEmailAndActive = sqlSelectJoinCustomers
            + " WHERE " + quote("email") + "=? AND " + quote("active") + "=?";
    private final String sqlSelectJoinEmployees = "SELECT * FROM " + quotedTableName
            + " JOIN " + employeesQuotedTableName
            + " ON " + quote("id") + "=" + quote("user_id");
    private final String sqlSelectJoinEmployeesOrderByEmail = sqlSelectJoinEmployees
            + " ORDER BY " + quote("email") + " ASC";
    private final String sqlSelectJoinEmployeeWhereId = sqlSelectJoinEmployees
            + " WHERE " + quote("id") + "=";
    private final String sqlSelectJoinEmployeeWhereEmailAndActive = sqlSelectJoinEmployees
            + " WHERE " + quote("email") + "=? AND " + quote("active") + "=?";
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
            + " SET " + quote("email") + "=?, " + quote("active") + "=?"
            + " WHERE " + quote("id") + "=";
    private final String sqlUpdateCustomer = "UPDATE " + customersQuotedTableName
            + " SET " + quote("user_id") + "=?, " + quote("balance") + "=?, "
            + quote("permitted_overdraft") + "=?, " + quote("payoff_date") + "=?"
            + " WHERE " + quote("user_id") + "=";
    private final String sqlUpdateEmployee = "UPDATE " + employeesQuotedTableName
            + " SET " + quote("user_id") + "=?, " + quote("role_id") + "=?"
            + " WHERE " + quote("user_id") + "=";

    public UserDaoJdbc(DataSource dataSource) {
        super(dataSource, USERS_TABLE_NAME, List.of("email", "password_hash", "active"));
    }

    @Override
    void mapObjectToRow(User user, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, user.getEmail());
            row.setBytes(++c, user.getPasswordHash());
            row.setBoolean(++c, user.isActive());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
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

    private void mapCustomerToRow(Customer customer, PreparedStatement row) {
        try {
            int c = 0;
            row.setLong(++c, customer.getId());
            row.setBigDecimal(++c, customer.getBalance());
            row.setBigDecimal(++c, customer.getPermittedOverdraft());
            LocalDateTime payoffDate = customer.getPayoffDate();
            row.setTimestamp(++c, payoffDate != null ? Timestamp.valueOf(payoffDate)
                                                     : null);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void mapEmployeeToRow(Employee employee, PreparedStatement row) {
        try {
            int c = 0;
            row.setLong(++c, employee.getId());
            row.setInt(++c, employee.getRole().ordinal());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
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
            executeUpdate(sqlInsertCustomer, row -> mapCustomerToRow(customer, row));
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            executeUpdate(sqlInsertEmployee, row -> mapEmployeeToRow(employee, row));
        } else {
            //FIXME: remove row from "users" table
            throw new RuntimeException("Unimplemented for " + user.getClass());
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Customer> findAllCustomers(long skip, int limit) {
        return (Iterable<Customer>) findMany(
                sqlSelectJoinCustomersOrderByEmail + " LIMIT " + limit + " OFFSET " + skip,
                this::mapRowsToCustomers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Employee> findAllEmployees(long skip, int limit) {
        return (Iterable<Employee>) findMany(
                sqlSelectJoinEmployeesOrderByEmail + " LIMIT " + limit + " OFFSET " + skip,
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
                statement -> {
                    try {
                        int c = 0;
                        statement.setString(++c, email);
                        statement.setBoolean(++c, active);
                    } catch (SQLException e) {
                        throw new DaoException(e);
                    }
                },
                this::mapRowToCustomer);
    }

    @SuppressWarnings("unchecked")
    Optional<Employee> findEmployeeByEmailAndActive(String email, boolean active) {
        return (Optional<Employee>) findOne(
                sqlSelectJoinEmployeeWhereEmailAndActive,
                statement -> {
                    try {
                        int c = 0;
                        statement.setString(++c, email);
                        statement.setBoolean(++c, active);
                    } catch (SQLException e) {
                        throw new DaoException(e);
                    }
                },
                this::mapRowToEmployee
        );
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
            int result = executeUpdate(
                    sqlUpdateUserWithoutHash + user.getId(),
                    row -> mapUserToRowWithoutHash(user, row));
            if (result < 1) {
                throw new DaoException("Could not update user");
            }
        }
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            int result = executeUpdate(sqlUpdateCustomer + user.getId(), row -> mapCustomerToRow(customer, row));
            if (result < 1) {
                throw new DaoException("Could not update customer");
            }
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            int result = executeUpdate(sqlUpdateEmployee + user.getId(), row -> mapEmployeeToRow(employee, row));
            if (result < 1) {
                throw new DaoException("Could not update employee");
            }
        } else {
            throw new DaoException("Unimplemented for " + user.getClass());
        }
    }
}
