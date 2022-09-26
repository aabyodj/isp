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
        services.put(TariffService.class, new TariffServiceImpl(context.getBean(TariffDao.class)));
        services.put(PromotionService.class, new PromotionServiceImpl(
                context.getBean(PromotionDao.class), context.getBean(ConfigManager.class)));
        services.put(SubscriptionService.class, new SubscriptionServiceImpl(
                context.getBean(SubscriptionDao.class),
                getService(TariffService.class)));
        services.put(UserService.class, new UserServiceImpl(
                context.getBean(UserDao.class),
                getService(TariffService.class),
                getService(SubscriptionService.class)));
        getService(UserService.class).createDefaultAdmin();
    }

    public void destroy() {
    }
    
}
