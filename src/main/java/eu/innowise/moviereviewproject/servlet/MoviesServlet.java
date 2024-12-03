package eu.innowise.moviereviewproject.servlet;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static eu.innowise.moviereviewproject.utils.ServletsUtil.parseInteger;

@Slf4j
@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {

    private final MovieService movieService;

    public MoviesServlet() {
        this.movieService = ApplicationConfig.getMovieService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            log.info("Received request for movies with parameters: page={}, typeNumber={}",
                    req.getParameter("page"), req.getParameter("typeNumber"));

            int page = parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE);
            int typeNumber = parseInteger(req.getParameter("typeNumber"), 0, 0, 5);

            log.info("Parsed request parameters: page={}, typeNumber={}", page, typeNumber);

            List<Movie> movies = movieService.getAllMovies(page, typeNumber);

            req.setAttribute("movies", movies);
            req.setAttribute("currentPage", page);
            req.setAttribute("currentTypeNumber", typeNumber);

            log.info("Movies fetched successfully. Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing request for movies.", e);
            throw new ServletException("Error fetching movies", e);
        }
    }
}
