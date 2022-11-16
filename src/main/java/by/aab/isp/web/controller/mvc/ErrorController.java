package by.aab.isp.web.controller.mvc;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import by.aab.isp.service.AccessDeniedException;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.log.Autologged;

@ControllerAdvice
@PermitAll
public class ErrorController {  //FIXME actual error codes are always 500

    @Autologged("ERROR")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String unhandledException(Throwable e, HttpServletRequest request) {
        return "error";
    }

    @Autologged("ERROR")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(MethodArgumentTypeMismatchException e) {   //FIXME clarify what exceptions to catch
        return "error/400";
    }

    @Autologged("WARN")
    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied() {
        return "error/403";
    }

    @Autologged("WARN")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e) {
        return "error/404";
    }
}
