package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {

    private static final String USERS_TABLE_NAME = "users";
    private static final String CUSTOMERS_TABLE_NAME = "customers";
    private static final String EMPLOYEES_TABLE_NAME = "employees";
    private static final String SQL_INSERT_CUSTOMER = "INSERT INTO " + CUSTOMERS_TABLE_NAME
            + " (user_id, balance, permitted_overdraft, payoff_date) VALUES (?, ?, ?, ?)";
    private static final int CUSTOMER_COLUMN_COUNT = 4;
    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO " + EMPLOYEES_TABLE_NAME
            + " (user_id, role_id) VALUES (?, ?)";
    private static final int EMPLOYEE_COLUMN_COUNT = 2;
    private static final String SQL_SELECT_JOIN_CUSTOMERS = "SELECT * FROM " + USERS_TABLE_NAME
            + " JOIN " + CUSTOMERS_TABLE_NAME + " ON id = user_id";
    private static final String SQL_SELECT_JOIN_CUSTOMER_WHERE_ID = SQL_SELECT_JOIN_CUSTOMERS
            + " WHERE id=";
    private static final String SQL_SELECT_JOIN_CUSTOMER_WHERE_EMAIL_AND_ACTIVE = SQL_SELECT_JOIN_CUSTOMERS
            + " WHERE email=? AND active=?";
    private static final String SQL_SELECT_JOIN_EMPLOYEES = "SELECT * FROM " + USERS_TABLE_NAME
            + " JOIN " + EMPLOYEES_TABLE_NAME + " ON id = user_id";
    private static final String SQL_SELECT_JOIN_EMPLOYEE_WHERE_ID = SQL_SELECT_JOIN_EMPLOYEES
            + " WHERE id=";
    private static final String SQL_SELECT_JOIN_EMPLOYEE_WHERE_EMAIL_AND_ACTIVE = SQL_SELECT_JOIN_EMPLOYEES
            + " WHERE email=? AND active=?";
    private static final String SQL_SELECT_CUSTOMERS_WHERE_ID = "SELECT * FROM " + CUSTOMERS_TABLE_NAME + " WHERE user_id=";
    private static final String SQL_SELECT_EMPLOYEES_WHERE_ID = "SELECT * FROM " + EMPLOYEES_TABLE_NAME + " WHERE user_id=";
    private static final String SQL_COUNT_JOIN_EMPLOYEES = "SELECT count(*) FROM " + USERS_TABLE_NAME
            + " JOIN " + EMPLOYEES_TABLE_NAME + " ON id = user_id";
    private static final String SQL_UPDATE_CUSTOMER = "UPDATE " + CUSTOMERS_TABLE_NAME
            + " SET user_id=?, balance=?, permitted_overdraft=?, payoff_date=? WHERE user_id=";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE " + EMPLOYEES_TABLE_NAME
            + " SET user_id=?, role_id=? WHERE user_id=";

    public UserDaoJdbc(DataSource dataSource) {
        super(dataSource, USERS_TABLE_NAME, List.of("email", "active"));
    }

    @Override
    void mapObjectToRow(User user, PreparedStatement row) {
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
            row.setTimestamp(++c, customer.getPayoffDate() != null ? Timestamp.from(customer.getPayoffDate())
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
            boolean active = row.getBoolean("active");
            @SuppressWarnings("unchecked") Optional<User> optional = (Optional<User>) findOne(SQL_SELECT_CUSTOMERS_WHERE_ID + id, this::mapRowToCustomer);
            if (optional.isEmpty()) {
                //noinspection unchecked
                optional = (Optional<User>) findOne(SQL_SELECT_EMPLOYEES_WHERE_ID + id, this::mapRowToEmployee);
            }
            User user = optional.orElseThrow(() ->
                    new DaoException("Inconsistent database: could not find neither Customer nor Employee for userId=" + id));
            user.setId(id);
            user.setEmail(email);
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
            user.setActive(row.getBoolean("active"));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Customer mapRowToCustomer(ResultSet row) {
        try {
            Customer customer = new Customer();
            if (row.getMetaData().getColumnCount() > CUSTOMER_COLUMN_COUNT) mapRowToUser(row, customer);
            customer.setBalance(row.getBigDecimal("balance"));
            customer.setPermittedOverdraft(row.getBigDecimal("permitted_overdraft"));
            Timestamp payoffDate = row.getTimestamp("payoff_date");
            customer.setPayoffDate(payoffDate != null ? payoffDate.toInstant()
                                                      : null);
            return customer;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Employee mapRowToEmployee(ResultSet row) {
        try {
            Employee employee = new Employee();
            if (row.getMetaData().getColumnCount() > EMPLOYEE_COLUMN_COUNT) mapRowToUser(row, employee);
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
    User objectWithId(User user, long id) {
        user.setId(id);
        return user;
    }

    @Override
    public User save(User user) {
        user = super.save(user);
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            executeUpdate(SQL_INSERT_CUSTOMER, row -> mapCustomerToRow(customer, row));
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            executeUpdate(SQL_INSERT_EMPLOYEE, row -> mapEmployeeToRow(employee, row));
        } else {
            //FIXME: remove row from "users" table
            throw new RuntimeException("Unimplemented for " + user.getClass());
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Customer> findAllCustomers() {
        return (Iterable<Customer>) findMany(SQL_SELECT_JOIN_CUSTOMERS, this::mapRowsToCustomers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Employee> findAllEmployees() {
        return (Iterable<Employee>) findMany(SQL_SELECT_JOIN_EMPLOYEES, this::mapRowsToEmployees);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Customer> findCustomerById(long id) {
        return (Optional<Customer>) findOne(SQL_SELECT_JOIN_CUSTOMER_WHERE_ID + id, this::mapRowToCustomer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Employee> findEmployeeById(long id) {
        return (Optional<Employee>) findOne(SQL_SELECT_JOIN_EMPLOYEE_WHERE_ID + id, this::mapRowToEmployee);
    }

    @SuppressWarnings("unchecked")
    Optional<Customer> findCustomerByEmailAndActive(String email, boolean active) {
        return (Optional<Customer>) findOne(
                SQL_SELECT_JOIN_CUSTOMER_WHERE_EMAIL_AND_ACTIVE,
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
                SQL_SELECT_JOIN_EMPLOYEE_WHERE_EMAIL_AND_ACTIVE,
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
    public long countByRoleAndActive(Employee.Role role, boolean active) {
        return count(SQL_COUNT_JOIN_EMPLOYEES
                + " WHERE role_id = " + role.ordinal()
                + " AND active = " + active);
    }

    @Override
    public long countByNotIdAndRoleAndActive(long id, Employee.Role role, boolean active) {
        return count(SQL_COUNT_JOIN_EMPLOYEES
                + " WHERE id != " + id
                + " AND role_id = " + role.ordinal()
                + " AND active = " + active);
    }

    @Override
    public void update(User user) {
        super.update(user);
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            int result = executeUpdate(SQL_UPDATE_CUSTOMER + user.getId(), row -> mapCustomerToRow(customer, row));
            if (result < 1) {
                throw new DaoException("Could not update customer");
            }
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            int result = executeUpdate(SQL_UPDATE_EMPLOYEE + user.getId(), row -> mapEmployeeToRow(employee, row));
            if (result < 1) {
                throw new DaoException("Could not update employee");
            }
        } else {
            throw new RuntimeException("Unimplemented for " + user.getClass());
        }
    }
}
