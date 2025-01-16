package eu.innowise.moviereviewproject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebFilter("/*")
public class LanguageFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();

        String lang = (String) session.getAttribute("lang");
        Locale locale = (lang == null) ? new Locale("") : new Locale(lang);

        ResourceBundle bundle = ResourceBundle.getBundle("resources", locale);

        req.setAttribute("bundle", bundle);

        chain.doFilter(req, res);
    }
}
