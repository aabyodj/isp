package by.aab.isp.dao;

import by.aab.isp.config.Config;
import by.aab.isp.dao.jdbc.*;
import by.aab.isp.entity.Employee;

import java.util.HashMap;
import java.util.Map;

public class DaoFactory {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
    private static final int DEFAULT_POOL_SIZE = 2;
    
    private final DataSource dataSource;
    private final Map<Class<? extends CrudRepository<?>>, CrudRepository<?>> repositories = new HashMap<>();
    
    private DaoFactory() {
        Config config = Config.getInstance();
        String url = config.getString("db.url");
        String user = config.getString("db.user");
        String password = config.getString("db.password");
        int poolSize = config.getInt("db.poolsize", DEFAULT_POOL_SIZE);
        dataSource = new SqlConnectionPool(url, user, password, poolSize);
        repositories.put(TariffDao.class, new TariffDaoJdbc(dataSource));
        repositories.put(UserDao.class, new UserDaoJdbc(dataSource));
        repositories.put(PromotionDao.class, new PromotionDaoJdbc(dataSource));
        repositories.put(SubscriptionDao.class, new SubscriptionDaoJdbc(
                dataSource,
                getDao(UserDao.class),
                getDao(TariffDao.class)));
    }
    
    private static class BillPughSingleton {
        static final DaoFactory INSTANCE = new DaoFactory();
    }
    
    public static DaoFactory getInstance() {
        return BillPughSingleton.INSTANCE;
    }
    
    public <T extends CrudRepository<?>> T getDao(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T result = (T) repositories.get(clazz);
        if (null == result) throw new IllegalStateException(clazz.getName() + " is not set");
        return result;
    }

    public void init() {
        //TODO: must create default admin somewhere else
        UserDao userDao = getDao(UserDao.class);
        if (userDao.countByRoleId(Employee.Role.ADMIN.ordinal()) < 1) {
            Employee defaultAdmin = new Employee();
            defaultAdmin.setEmail(DEFAULT_ADMIN_EMAIL);
            defaultAdmin.setRole(Employee.Role.ADMIN);
            userDao.save(defaultAdmin);
        }
    }

    public void destroy() {
        dataSource.close();
    }

}
