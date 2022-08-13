package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {

    private static final String USERS_TABLE_NAME = "users";
    private static final String CUSTOMERS_TABLE_NAME = "customers";
    private static final String EMPLOYEES_TABLE_NAME = "employees";
    private static final String SQL_INSERT_CUSTOMER = "INSERT INTO " + CUSTOMERS_TABLE_NAME
            + " (user_id, tariff_id, balance, permitted_overdraft, payoff_date) VALUES (?, ?, ?, ?, ?)";
    private static final int CUSTOMER_COLUMN_COUNT = 5;
    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO " + EMPLOYEES_TABLE_NAME
            + " (user_id, role_id) VALUES (?, ?)";
    private static final int EMPLOYEE_COLUMN_COUNT = 2;
    private static final String SQL_SELECT_JOIN_CUSTOMERS = "SELECT * FROM " + USERS_TABLE_NAME
            + " JOIN " + CUSTOMERS_TABLE_NAME + " ON id = user_id";
    private static final String SQL_SELECT_JOIN_EMPLOYEES = "SELECT * FROM " + USERS_TABLE_NAME
            + " JOIN " + EMPLOYEES_TABLE_NAME + " ON id = user_id";
    private static final String SQL_SELECT_CUSTOMERS_WHERE_ID = "SELECT * FROM " + CUSTOMERS_TABLE_NAME + " WHERE user_id=";
    private static final String SQL_SELECT_EMPLOYEES_WHERE_ID = "SELECT * FROM " + EMPLOYEES_TABLE_NAME + " WHERE user_id=";
    private static final String SQL_COUNT_EMPLOYEES_WHERE_ROLE_ID = "SELECT count(*) FROM employees WHERE role_id=";
    private static final String SQL_UPDATE_CUSTOMER = "UPDATE " + CUSTOMERS_TABLE_NAME
            + " SET user_id=?, tariff_id=?, balance=?, permitted_overdraft=?, payoff_date=? WHERE user_id=";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE " + EMPLOYEES_TABLE_NAME
            + " SET user_id=?, role_id=? WHERE user_id=";

    private final TariffDao tariffDao;

    public UserDaoJdbc(DataSource dataSource, TariffDao tariffDao) {
        super(dataSource, USERS_TABLE_NAME, List.of("email"));
        this.tariffDao = tariffDao;
    }

    @Override
    void mapObjectToRow(User user, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, user.getEmail());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void mapCustomerToRow(Customer customer, PreparedStatement row) {
        try {
            int c = 0;
            row.setLong(++c, customer.getId());
            row.setLong(++c, customer.getTariff() != null ? customer.getTariff().getId()
                                                          : 0);
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
            Optional<User> optional = findOne(SQL_SELECT_CUSTOMERS_WHERE_ID + id, this::mapRowToCustomer);
            if (optional.isEmpty()) {
                optional = findOne(SQL_SELECT_EMPLOYEES_WHERE_ID + id, this::mapRowToEmployee);
            }
            User user = optional.orElseThrow();
            user.setId(id);
            user.setEmail(email);
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void mapRowToUser(ResultSet row, User user) {
        try {
            user.setId(row.getLong("id"));
            user.setEmail(row.getString("email"));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Customer mapRowToCustomer(ResultSet row) {
        try {
            Customer customer = new Customer();
            if (row.getMetaData().getColumnCount() > CUSTOMER_COLUMN_COUNT) mapRowToUser(row, customer);
            long tariffId = row.getLong("tariff_id");
            customer.setTariff(tariffId != 0 ? tariffDao.findById(tariffId).orElseThrow()
                                             : null);
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

    private final String sqlSelectWhereEmail = sqlSelect + " WHERE email = ?";

    //TODO: implement findById(), findAll() and findByEmail() via SELECT JOIN
    @Override
    public Optional<User> findByEmail(String email) {
        return findOne(
                sqlSelectWhereEmail,
                statement -> {
                    try {
                        statement.setString(1, email);
                    } catch (SQLException e) {
                        throw new DaoException(e);
                    }
                },
                this::mapRowToObject);
    }

    @Override
    public long countByRoleId(long roleId) {
        return count(SQL_COUNT_EMPLOYEES_WHERE_ROLE_ID + roleId);
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
