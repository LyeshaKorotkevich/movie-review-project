package eu.innowise.moviereviewproject.filter;

import eu.innowise.moviereviewproject.dto.response.UserResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/review", "/watchlist"})
public class SecurityFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        UserResponse userResponse = (UserResponse) req.getSession().getAttribute("user");

        if (userResponse == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        chain.doFilter(req, res);
    }
}