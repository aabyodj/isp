package by.aab.isp.dao;

import java.util.HashMap;
import java.util.Map;

import by.aab.isp.config.Config;
import by.aab.isp.dao.jdbc.DataSource;
import by.aab.isp.dao.jdbc.SqlConnectionPool;
import by.aab.isp.dao.jdbc.TariffDaoJdbc;

public class DaoFactory {
    
    private final DataSource dataSource;
    private final Map<Class<?>, Object> repositories = new HashMap<>();
    
    private DaoFactory() {
        Config config = Config.getInstance();
        String url = config.getString("db.url");
        String user = config.getString("db.user");
        String password = config.getString("db.password");
        int poolSize = config.getInt("db.poolsize", 1);
        dataSource = new SqlConnectionPool(url, user, password, poolSize);
        repositories.put(TariffDao.class, new TariffDaoJdbc(dataSource));
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

    public void destroy() {
        dataSource.close();
    }

}
