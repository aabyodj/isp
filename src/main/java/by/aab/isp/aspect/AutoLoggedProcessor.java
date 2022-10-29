package by.aab.isp.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AutoLoggedProcessor {

    @Before(value = "@annotation(AutoLogged)", argNames = "value")
    public void before(JoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        AutoLogged annotation = method.getAnnotation(AutoLogged.class);
        Level level = Level.valueOf(annotation.value());
        Class<?> clazz = signature.getDeclaringType();
        Logger log = LogManager.getLogger(clazz);
        log.log(level, jp.getSignature() + Arrays.toString(jp.getArgs()));
    }
}
