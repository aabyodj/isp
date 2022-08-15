package by.aab.isp.web;

import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public final class Controller extends HttpServlet {

    public static final String SCHEMA_REDIRECT = "redirect:";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private static void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("action");
        CommandFactory commandFactory = CommandFactory.getInstance();
        Command command = commandFactory.getCommand(commandName);
        String path = command.apply(req);
        if (path.startsWith(SCHEMA_REDIRECT)) {
            resp.sendRedirect(path.substring(SCHEMA_REDIRECT.length()));
        } else {
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        CommandFactory.getInstance().init();
    }

    @Override
    public void destroy() {
        CommandFactory.getInstance().destroy();
    }
    
}
