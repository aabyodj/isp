package by.aab.isp.aspect;

import java.util.Arrays;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AutoLoggedProcessor {

    @Before("@annotation(autoLogged)")
    public void before(JoinPoint jp, AutoLogged autoLogged) {
        Signature signature = jp.getSignature();
        Class<?> clazz = signature.getDeclaringType();
        Logger log = LogManager.getLogger(clazz);
        Level level = Level.valueOf(autoLogged.value());
        log.log(level, signature + Arrays.toString(jp.getArgs()));
    }
}
