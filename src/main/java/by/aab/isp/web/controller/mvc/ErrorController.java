package by.aab.isp.web.controller.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import by.aab.isp.service.AccessDeniedException;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.dto.user.UserViewDto;
import by.aab.isp.service.log.Autologged;

@ControllerAdvice
public class ErrorController {

    @Autologged("ERROR")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String unhandledException(Throwable e, HttpServletRequest request) {
        return "error";
    }

    @Autologged("ERROR")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return "error/400";
    }

    @Autologged("WARN")
    @ExceptionHandler(AccessDeniedException.class)  //FIXME this doesn't work
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied(HttpServletRequest request, @RequestAttribute(required = false) UserViewDto activeUser) {
        return "error/403";
    }

    @Autologged("WARN")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e, HttpServletRequest request) {
        return "error/404";
    }
}
