package by.aab.isp.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class Logger {

    @Before("@annotation(AutoLogged)")
    public void before(JoinPoint jp) {
        log.trace(jp.getSignature() + Arrays.toString(jp.getArgs()));
    }
}
