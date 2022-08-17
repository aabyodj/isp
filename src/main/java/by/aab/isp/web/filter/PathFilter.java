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
public class PathFilter extends HttpFilter {

    private static final Set<String> ALLOWED_PATH_SET = Set.of(
            "/", "/css/"
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getServletPath();
        path = path.substring(0, path.lastIndexOf('/') + 1);
        if (ALLOWED_PATH_SET.contains(path)) {
            log.trace("Accepted path '" + path + "'");
            chain.doFilter(req, res);
        } else {
            log.warn("Rejected path '" + path +"'");
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
