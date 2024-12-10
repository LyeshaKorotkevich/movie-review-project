package eu.innowise.moviereviewproject.servlet.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        try {
            resp.sendRedirect(req.getContextPath() + "/movies");
        } catch (IOException e) {
            log.error("Error occurred while processing redirect", e);
        }
    }
}
