package eu.innowise.moviereviewproject.servlet.auth;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.request.RegistrationRequest;
import eu.innowise.moviereviewproject.exceptions.DtoValidationException;
import eu.innowise.moviereviewproject.exceptions.user.UserAlreadyExistsException;
import eu.innowise.moviereviewproject.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static eu.innowise.moviereviewproject.utils.Constants.AUTH_LOGIN_URL;
import static eu.innowise.moviereviewproject.utils.ServletsUtil.getRegistrationRequest;

@WebServlet("/auth/register")
public class RegistrationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = ApplicationConfig.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegistrationRequest registrationRequest = getRegistrationRequest(req);

        try {
            userService.registerUser(registrationRequest);
            resp.sendRedirect(req.getContextPath() + AUTH_LOGIN_URL);
        } catch (DtoValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("registrationDTO", registrationRequest);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        } catch (UserAlreadyExistsException e) {
            req.setAttribute("userAlreadyExists", "Username or email is already taken");
            req.setAttribute("registrationDTO", registrationRequest);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error occurred during registration", e);
        }
    }
}
