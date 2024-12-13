package eu.innowise.moviereviewproject.filter;

import eu.innowise.moviereviewproject.dto.response.UserResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static eu.innowise.moviereviewproject.utils.Constants.AUTH_LOGIN_URL;

@WebFilter("/admin/*")
public class RoleCheckFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        UserResponse userResponse = (UserResponse) req.getSession().getAttribute("user");

        if (Objects.isNull(userResponse) || !Objects.equals(userResponse.userRole(), "ADMIN")) {
            res.sendRedirect(req.getContextPath() + AUTH_LOGIN_URL);
            return;
        }

        chain.doFilter(req, res);
    }
}
