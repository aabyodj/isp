package by.aab.isp.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class PathFilter extends HttpFilter {

    private static final Set<String> ALLOWED_PATH_SET = Set.of(
            "/", "/css/"
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getServletPath();
        path = path.substring(0, path.lastIndexOf('/') + 1);
        if (ALLOWED_PATH_SET.contains(path)) {
            chain.doFilter(req, res);
            return;
        }
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
