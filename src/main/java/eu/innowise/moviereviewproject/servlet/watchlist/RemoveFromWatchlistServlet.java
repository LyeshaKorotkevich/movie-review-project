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

import static eu.innowise.moviereviewproject.utils.Constants.WATCHLIST_URL;

@Slf4j
@WebServlet("/watchlist/remove")
public class RemoveFromWatchlistServlet extends HttpServlet {

    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        this.watchlistService = ApplicationConfig.getWatchlistService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String movieIdParam = req.getParameter("movieId");

        if (movieIdParam != null) {
            try {
                UUID movieId = UUID.fromString(movieIdParam);
                UserResponse user = (UserResponse) req.getSession().getAttribute("user");

                watchlistService.removeMovieFromUserWatchlist(user.id(), movieId);

                res.sendRedirect(req.getContextPath() + WATCHLIST_URL);
            } catch (Exception e) {
                log.error("Unexpected error during removing from watchlist process", e);
            }
        }
    }
}
