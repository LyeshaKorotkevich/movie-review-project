package eu.innowise.moviereviewproject.servlet;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.LoginDTO;
import eu.innowise.moviereviewproject.dto.UserDTO;
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

    private final AuthenticationService authenticationService;

    public LoginServlet() {
        this.authenticationService = ApplicationConfig.getAuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginDTO loginDTO = new LoginDTO(req.getParameter("username"), req.getParameter("password"));

        try {
            UserDTO authenticatedUser = authenticationService.authenticate(loginDTO);

            log.info("Authentication successful for user {}", loginDTO.username());

            HttpSession session = req.getSession();
            session.setAttribute("user", authenticatedUser);

            log.debug("Redirecting user {} to /movies",  loginDTO.username());
            resp.sendRedirect(req.getContextPath() + "/movies");
        } catch (UserNotFoundException | IllegalArgumentException e) {
            log.warn("Authentication failed for user {}",  loginDTO.username(), e);
            req.setAttribute("errorMessage", "Неверный логин или пароль");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Unexpected error during login process", e);
            req.setAttribute("errorMessage", "Произошла ошибка, попробуйте позже");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
