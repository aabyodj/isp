package by.aab.isp.service;

import by.aab.isp.dao.*;
import by.aab.isp.service.impl.PromotionServiceImpl;
import by.aab.isp.service.impl.SubscriptionServiceImpl;
import by.aab.isp.service.impl.TariffServiceImpl;
import by.aab.isp.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
    
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    
    private ServiceFactory() {
        services.put(TariffService.class, new TariffServiceImpl(daoFactory.getDao(TariffDao.class)));
        services.put(UserService.class, new UserServiceImpl(daoFactory.getDao(UserDao.class)));
        services.put(PromotionService.class, new PromotionServiceImpl(daoFactory.getDao(PromotionDao.class)));
        services.put(SubscriptionService.class, new SubscriptionServiceImpl(
                daoFactory.getDao(SubscriptionDao.class),
                getService(TariffService.class)));
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
        if (null == result) throw new IllegalStateException(clazz.getName() + " is not set");
        return result;
    }

    public void init() {
        daoFactory.init();
    }

    public void destroy() {
        daoFactory.destroy();
    }
    
}
