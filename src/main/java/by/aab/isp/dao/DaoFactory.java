package by.aab.isp.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import by.aab.isp.dao.jdbc.DataSource;
import by.aab.isp.dao.jdbc.PromotionDaoJdbc;
import by.aab.isp.dao.jdbc.SubscriptionDaoJdbc;
import by.aab.isp.dao.jdbc.TariffDaoJdbc;
import by.aab.isp.dao.jdbc.UserDaoJdbc;

public class DaoFactory {
    
    private DataSource dataSource;
    private final Map<Class<? extends CrudRepository<?>>, CrudRepository<?>> repositories = new HashMap<>();
    
    private DaoFactory() {
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
        if (null == result) {
            throw new IllegalStateException(clazz.getName() + " is not set");
        }
        return result;
    }

    public void init(ApplicationContext context) {
        dataSource = context.getBean(DataSource.class);
        repositories.put(TariffDao.class, new TariffDaoJdbc(dataSource));
        repositories.put(UserDao.class, new UserDaoJdbc(dataSource));
        repositories.put(PromotionDao.class, new PromotionDaoJdbc(dataSource));
        repositories.put(SubscriptionDao.class, new SubscriptionDaoJdbc(
                dataSource,
                getDao(UserDao.class),
                getDao(TariffDao.class)));
    }

    public void destroy() {
    }

}
