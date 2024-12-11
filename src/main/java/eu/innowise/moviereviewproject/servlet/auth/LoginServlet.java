package eu.innowise.moviereviewproject.servlet.auth;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.request.LoginRequest;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.exceptions.DtoValidationException;
import eu.innowise.moviereviewproject.exceptions.user.InvalidPasswordException;
import eu.innowise.moviereviewproject.exceptions.user.UserNotFoundException;
import eu.innowise.moviereviewproject.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private AuthenticationService authenticationService;

    @Override
    public void init() throws ServletException {
        this.authenticationService = ApplicationConfig.getAuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginRequest loginRequest = new LoginRequest(req.getParameter("username"), req.getParameter("password"));

        try {
            UserResponse authenticatedUser = authenticationService.authenticate(loginRequest);

            log.info("Authentication successful for user {}", loginRequest.username());

            HttpSession session = req.getSession();
            session.setAttribute("user", authenticatedUser);

            log.debug("Redirecting user {} to /movies", loginRequest.username());
            resp.sendRedirect(req.getContextPath() + "/movies");
        } catch (DtoValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (UserNotFoundException | InvalidPasswordException e) {
            log.warn("Authentication failed for user {}", loginRequest.username());
            req.setAttribute("userNotExists", "Неверный логин или пароль");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Unexpected error during login process", e);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
