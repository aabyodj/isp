package by.aab.isp.aspect;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

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
        String requestUrl = Arrays.stream(jp.getArgs())
                .filter(arg -> arg instanceof HttpServletRequest)
                .map(arg -> System.lineSeparator() + "Request URL is: " + ((HttpServletRequest) arg).getRequestURL())
                .findFirst()
                .orElseGet(() -> "");
        String message = signature + Arrays.toString(jp.getArgs()) + requestUrl;
        Arrays.stream(jp.getArgs())
                .filter(arg -> arg instanceof Throwable)
                .findFirst()
                .ifPresentOrElse(
                        throwable -> log.log(level, message, throwable),
                        () -> log.log(level, message));
    }


}
