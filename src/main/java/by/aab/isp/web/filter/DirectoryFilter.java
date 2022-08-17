package by.aab.isp.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Set;

@Log4j2
public class DirectoryFilter extends HttpFilter {

    private static final Set<String> ALLOWED_DIRECTORIES = Set.of(
            "/", "/css/"
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String directory = req.getServletPath();
        directory = directory.substring(0, directory.lastIndexOf('/') + 1);
        if (ALLOWED_DIRECTORIES.contains(directory)) {
            log.trace("Accepted directory '" + directory + "'");
            chain.doFilter(req, res);
        } else {
            log.warn("Rejected directory '" + directory +"'");
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
