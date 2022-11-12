package by.aab.isp.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FallbackMethodSecurityMetadataSource extends AbstractFallbackMethodSecurityMetadataSource {

    private static final List<ConfigAttribute> DENY_ALL = List.of(() -> "DENY_ALL");
    private static final Set<Class<? extends Annotation>> CONTROLLER_ANNOTATIONS = Set.of(Controller.class, RestController.class);
    private static final Set<Class<? extends Annotation>> SECURITY_ANNOTATIONS = Set.of(    //TODO take into consideration only those actually enabled
            PreAuthorize.class, PostAuthorize.class, PreFilter.class, PostFilter.class,
            RolesAllowed.class, PermitAll.class, DenyAll.class,
            Secured.class);

    @Override
    protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
        if (!isDesiredClass(targetClass)) {
            return null;
        };
        for (Annotation annotation : method.getAnnotations()) {
            if (SECURITY_ANNOTATIONS.contains(annotation.annotationType())) {
                return null;
            }
        }
        log.debug("Added fallback attribute \"DENY_ALL\" to method \"" + method + "\" in class \"" + targetClass + "\"");
        return DENY_ALL;
    }

    private boolean isDesiredClass(Class<?> clazz) {
        boolean isController = false;
        for (Annotation annotation : clazz.getAnnotations()) {
            if (CONTROLLER_ANNOTATIONS.contains(annotation.annotationType())) {
                isController = true;
            } else if (SECURITY_ANNOTATIONS.contains(annotation.annotationType())) {
                return false;
            }
        }
        return isController;
    }

    @Override
    protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
}
