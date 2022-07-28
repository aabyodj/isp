package by.aab.isp.web.command;

import java.util.function.Function;

import jakarta.servlet.http.HttpServletRequest;

public interface Command extends Function<HttpServletRequest, String> {
}
