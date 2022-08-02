package by.aab.isp.web;

import java.io.IOException;

import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class Controller extends HttpServlet {    


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("action");
        Command command = CommandFactory.getInstance().getCommand(commandName);
        req.getRequestDispatcher(command.apply(req)).forward(req, resp);
    }
    
    @Override
    public void destroy() {
        CommandFactory.getInstance().destroy();
    }
    
}
