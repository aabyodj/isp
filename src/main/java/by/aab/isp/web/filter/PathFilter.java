package by.aab.isp.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Set;

@Log4j2
public class PathFilter extends HttpFilter {

    private static final Set<String> ALLOWED_PASSTHROUGH_DIRECTORIES = Set.of("/css/");

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getServletPath();
        if ("/".equals(path)) {
            log.trace("Accepted path '/'");
            chain.doFilter(req, res);
            return;
        }
        String directory = path.substring(0, path.lastIndexOf('/') + 1);
        if (ALLOWED_PASSTHROUGH_DIRECTORIES.contains(directory)) {
            log.trace("Accepted directory '" + directory + "'");
            chain.doFilter(req, res);
            return;
        }
        log.warn("Rejected path '" + path +"'");
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
