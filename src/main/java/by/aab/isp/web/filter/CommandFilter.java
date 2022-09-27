package by.aab.isp.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import by.aab.isp.web.command.Command;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommandFilter extends HttpFilter {

    private ApplicationContext context;
    Command homeCommand;

    @Override
    public void init() throws ServletException {
        try {
            log.trace("Initializing...");
            context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            homeCommand = (Command) context.getBean("homeCommand");
            log.info("Initialization complete");
        } catch (Throwable e) {
            log.fatal("Failed to initialize", e);
            throw e;
        }
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String commandName = req.getParameter("action");
        try {
            Command command = getCommand(commandName);
            req.setAttribute("command", command);
            log.trace("Found command '" + command.getClass() + "' for name '" + commandName + "'");
            chain.doFilter(req, res);
        } catch (NoSuchBeanDefinitionException | ClassCastException e) {
            log.error("Unknown command '" + commandName + "'");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private Command getCommand(String commandName) {
        if (null == commandName || commandName.isBlank()) {
            return homeCommand;
        }
        return (Command) context.getBean(commandName);
    }
}
