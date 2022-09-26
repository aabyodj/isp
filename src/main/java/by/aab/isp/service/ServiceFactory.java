package by.aab.isp.service;

import by.aab.isp.config.ConfigManager;
import by.aab.isp.dao.*;
import by.aab.isp.service.impl.PromotionServiceImpl;
import by.aab.isp.service.impl.SubscriptionServiceImpl;
import by.aab.isp.service.impl.TariffServiceImpl;
import by.aab.isp.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public class ServiceFactory {
    
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    
    private ServiceFactory() {
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
        if (null == result) {
            throw new IllegalStateException(clazz.getName() + " is not set");
        }
        return result;
    }

    public void init(ApplicationContext context) {
        daoFactory.init(context);
        services.put(TariffService.class, new TariffServiceImpl(daoFactory.getDao(TariffDao.class)));
        services.put(PromotionService.class, new PromotionServiceImpl(
                daoFactory.getDao(PromotionDao.class), context.getBean(ConfigManager.class)));
        services.put(SubscriptionService.class, new SubscriptionServiceImpl(
                daoFactory.getDao(SubscriptionDao.class),
                getService(TariffService.class)));
        services.put(UserService.class, new UserServiceImpl(
                daoFactory.getDao(UserDao.class),
                getService(TariffService.class),
                getService(SubscriptionService.class)));
        getService(UserService.class).createDefaultAdmin();
    }

    public void destroy() {
        daoFactory.destroy();
    }
    
}
