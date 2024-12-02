package eu.innowise.moviereviewproject.servlet;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.service.MovieService;
import eu.innowise.moviereviewproject.utils.ServletsUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebServlet("/movies/*")
public class MovieDetailsServlet extends HttpServlet {

    private final MovieService movieService;

    public MovieDetailsServlet() {
        this.movieService = ApplicationConfig.getMovieService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {

            UUID movieId = ServletsUtil.extractUuidFromPath(req, resp, 2, 2);

            log.info("Extracted movieId={}", movieId);

            Movie movie = movieService.getMovieById(movieId);

            req.setAttribute("movie", movie);

            log.info("Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movie-details.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing request for movies.", e);
            throw new ServletException("Error getting movie", e);
        }
    }
}
