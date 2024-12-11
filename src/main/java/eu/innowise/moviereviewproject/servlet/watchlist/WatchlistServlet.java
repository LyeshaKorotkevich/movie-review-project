package eu.innowise.moviereviewproject.servlet.watchlist;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.dto.response.WatchlistResponse;
import eu.innowise.moviereviewproject.model.Watchlist;
import eu.innowise.moviereviewproject.service.WatchlistService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.ServletsUtil.parseInteger;

@Slf4j
@WebServlet("/watchlist")
public class WatchlistServlet extends HttpServlet {

    private final WatchlistService watchlistService;

    public WatchlistServlet() {
        this.watchlistService = ApplicationConfig.getWatchlistService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponse currentUser = (UserResponse) req.getSession().getAttribute("user");
        int page = parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE);

        List<WatchlistResponse> watchlist = watchlistService.getUserWatchlist(page, currentUser.id());

        for (WatchlistResponse watchlistResponse : watchlist) {
            log.info("Watchlist response: {}", watchlistResponse.isWatched());
        }

        req.setAttribute("page", page);
        req.setAttribute("watchlist", watchlist);

        req.getRequestDispatcher("/WEB-INF/views/watchlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            UserResponse currentUser = (UserResponse) req.getSession().getAttribute("user");

            UUID movieId = UUID.fromString(req.getParameter("movieId"));

            watchlistService.addMovieToWatchlist(currentUser.id(), movieId);

            res.sendRedirect(req.getContextPath() + "/movies/" + movieId);
        } catch (Exception e) {
            log.error("Error during adding movie to watchlist: {}", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/movies");
        }
    }
}