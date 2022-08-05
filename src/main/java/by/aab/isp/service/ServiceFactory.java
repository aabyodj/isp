package by.aab.isp.service;

import java.util.HashMap;
import java.util.Map;

import by.aab.isp.dao.DaoFactory;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.dao.UserDao;
import by.aab.isp.service.impl.TariffServiceImpl;
import by.aab.isp.service.impl.UserServiceImpl;

public class ServiceFactory {
    
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    
    private ServiceFactory() {
        services.put(TariffService.class, new TariffServiceImpl(daoFactory.getDao(TariffDao.class)));
        services.put(UserService.class, new UserServiceImpl(daoFactory.getDao(UserDao.class)));
    }
    
    private static class BillPughSingleton {
        static final ServiceFactory INSTANCE = new ServiceFactory();
    }
    
    public static ServiceFactory getInstance() {
        return BillPughSingleton.INSTANCE;
    }

    public <T> T getService(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T result = (T) services.get(clazz);
        if (null == result) throw new IllegalArgumentException();
        return result;
    }

    public void init() {
        daoFactory.init();
    }

    public void destroy() {
        daoFactory.destroy();
    }
    
}
