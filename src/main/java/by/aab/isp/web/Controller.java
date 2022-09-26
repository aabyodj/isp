package by.aab.isp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class Controller extends HttpServlet {

    public static final String SCHEMA_REDIRECT = "redirect:";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Processing GET request '" + req.getRequestURL() + "'");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Processing POST request '" + req.getRequestURL() + "'");
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Command command = (Command) req.getAttribute("command");
            if (null == command) {  //TODO: move this to Filter
                log.warn("Rejected file '" + req.getServletPath() + "'");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String path = command.execute(req);
            if (path.startsWith(SCHEMA_REDIRECT)) {
                String redirect = path.substring(SCHEMA_REDIRECT.length());
                log.trace("Redirecting to '" + redirect + "'");
                resp.sendRedirect(redirect);
            } else {
                log.trace("Forwarding to '" + path + "'");
                req.getRequestDispatcher(path).forward(req, resp);
            }
        } catch (UnauthorizedException e) {
            log.warn("Login failure. Email = '" + e.getMessage() + "'");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);    //TODO: put a message on the login form
        } catch (Throwable e) {
            log.error("Error processing request '" + req.getRequestURL() + "'", e);
            throw e;
        }
    }

    @Override
    public void destroy() {
        try {
            log.trace("Destroying the controller...");
            CommandFactory.getInstance().destroy();
            log.info("Destruction complete");
        } catch (Throwable e) {
            log.error("Failed to destroy the controller", e);
            throw e;
        }
    }
    
}
