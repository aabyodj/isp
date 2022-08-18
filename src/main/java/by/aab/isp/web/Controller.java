package by.aab.isp.web;

import by.aab.isp.web.command.Command;
import by.aab.isp.web.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

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
        } catch (Throwable e) {
            log.error("Error processing request '" + req.getRequestURL() + "'", e);
            throw e;
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            log.trace("Initializing the controller...");
            CommandFactory.getInstance().init();
            log.info("Initialization complete");
        } catch (Throwable e) {
            log.fatal("Failed to initialize", e);
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
