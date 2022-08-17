package by.aab.isp.web.filter;

import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.NoSuchElementException;

@Log4j2
public class CommandFilter extends HttpFilter {

    private CommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        try {
            log.trace("Initializing...");
            commandFactory = CommandFactory.getInstance();
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
            Command command = commandFactory.getCommand(commandName);
            req.setAttribute("command", command);
            log.trace("Found command '" + command.getClass().getName() + "' for name '" + commandName + "'");
            chain.doFilter(req, res);
        } catch (NoSuchElementException e) {
            log.error("Unknown command '" + commandName + "'");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
