package eu.innowise.moviereviewproject.servlet.watchlist;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.service.WatchlistService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/watchlist/mark-watched")
public class MarkWatchedServlet extends HttpServlet {
    private WatchlistService watchlistService;

    public MarkWatchedServlet() {
        this.watchlistService = ApplicationConfig.getWatchlistService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String movieIdParam = req.getParameter("movieId");

        if (movieIdParam != null) {
            try {
                UUID movieId = UUID.fromString(movieIdParam);
                UserResponse user = (UserResponse) req.getSession().getAttribute("user");

                watchlistService.markAsWatched(user.id(), movieId);

                res.sendRedirect(req.getContextPath() + "/watchlist");
            } catch (Exception e) {
                log.error("Unexpected error during removing from watchlist process", e);
            }
        }
    }
}

