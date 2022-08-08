package by.aab.isp.dao;

import java.util.HashMap;
import java.util.Map;

import by.aab.isp.config.Config;
import by.aab.isp.dao.jdbc.*;
import by.aab.isp.entity.User;

public class DaoFactory {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
    
    private final DataSource dataSource;
    private final Map<Class<?>, CrudRepository<?>> repositories = new HashMap<>();
    
    private DaoFactory() {
        Config config = Config.getInstance();
        String url = config.getString("db.url");
        String user = config.getString("db.user");
        String password = config.getString("db.password");
        int poolSize = config.getInt("db.poolsize", 1);
        dataSource = new SqlConnectionPool(url, user, password, poolSize);
        repositories.put(TariffDao.class, new TariffDaoJdbc(dataSource));
        repositories.put(UserDao.class, new UserDaoJdbc(dataSource));
        repositories.put(CustomerAccountDao.class, new CustomerAccountDaoJdbc(
                dataSource, getDao(TariffDao.class), getDao(UserDao.class)));
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
        if (null == result) throw new IllegalArgumentException();
        return result;
    }

    public void init() {
//        repositories.values().forEach(CrudRepository::init);

        //TODO: must create default admin somewhere else
        UserDao userDao = getDao(UserDao.class);
        if (userDao.countByRoleId(User.Role.ADMIN.ordinal()) < 1) {
            User defaultAdmin = new User();
            defaultAdmin.setEmail(DEFAULT_ADMIN_EMAIL);
            defaultAdmin.setRole(User.Role.ADMIN);
            userDao.save(defaultAdmin);
        }
    }

    public void destroy() {
        dataSource.close();
    }

}
