package eu.innowise.moviereviewproject.servlet.movie;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.service.GenreService;
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
@WebServlet("/movies/filter")
public class MoviesFilterServlet extends HttpServlet {

    private final MovieService movieService;
    private final GenreService genreService;

    public MoviesFilterServlet() {
        this.movieService = ApplicationConfig.getMovieService();
        this.genreService = ApplicationConfig.getGenreService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            log.info("Received request for filtered movies with parameters: genre={}, startYear={}, endYear={}, minRating={}, maxRating={}, typeNumber={}, page={}",
                    req.getParameter("genre"), req.getParameter("startYear"), req.getParameter("endYear"),
                    req.getParameter("minRating"), req.getParameter("maxRating"), req.getParameter("typeNumber"), req.getParameter("page"));

            String genre = req.getParameter("genre");
            Integer startYear = parseInteger(req.getParameter("startYear"), null, 1895, 2100);
            Integer endYear = parseInteger(req.getParameter("endYear"), null, 1895, 2100);
            Integer minRating = parseInteger(req.getParameter("minRating"), null, 1, 10);
            Integer maxRating = parseInteger(req.getParameter("maxRating"), null, 1, 10);
            Integer typeNumber = parseInteger(req.getParameter("typeNumber"), null, 0, 5);
            Integer page = parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE);

            log.info("Parsed filter parameters: genre={}, startYear={}, endYear={}, minRating={}, maxRating={}, typeNumber={}, page={}",
                    genre, startYear, endYear, minRating, maxRating, typeNumber, page);

            List<MovieResponse> filteredMovies = movieService.getFilteredMovies(page, typeNumber, genre, startYear, endYear, minRating, maxRating);

            req.setAttribute("movies", filteredMovies);
            req.setAttribute("currentPage", page);
            req.setAttribute("currentTypeNumber", typeNumber);
            req.setAttribute("genres", genreService.getAll());

            log.info("Filtered movies fetched successfully. Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing filtered movies request.", e);
            throw new ServletException("Error fetching filtered movies", e);
        }
    }
}
