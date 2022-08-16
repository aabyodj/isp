package by.aab.isp.web.filter;

import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CommandFilter extends HttpFilter {

    private CommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        commandFactory = CommandFactory.getInstance();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String commandName = req.getParameter("action");
        Command command = commandFactory.getCommand(commandName);
        req.setAttribute("command", command);
        chain.doFilter(req, res);
    }
}
