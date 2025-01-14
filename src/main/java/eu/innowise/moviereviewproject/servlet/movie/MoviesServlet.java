package eu.innowise.moviereviewproject.servlet.movie;

import eu.innowise.moviereviewproject.dto.request.MovieFilterRequest;
import eu.innowise.moviereviewproject.dto.response.GenreResponse;
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

import static eu.innowise.moviereviewproject.utils.ServletsUtil.getMovieFilterRequest;

@Slf4j
@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {

    private MovieService movieService;
    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        this.movieService = MovieService.getInstance();
        this.genreService = GenreService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            MovieFilterRequest movieFilterRequest = getMovieFilterRequest(req);

            log.info("Parsed request parameters: {}", movieFilterRequest);

            List<MovieResponse> movies;
            if (movieFilterRequest.genre() != null || movieFilterRequest.startYear() != null || movieFilterRequest.endYear() != null ||
                    movieFilterRequest.minRating() != null || movieFilterRequest.maxRating() != null) {
                movies = movieService.getFilteredMovies(movieFilterRequest);
                log.info("Applied filters and fetched filtered movies.");
            } else {
                movies = movieService.getAllMovies(movieFilterRequest.page(), movieFilterRequest.typeNumber());
                log.info("No filters applied. Fetched all movies.");
            }
            List<GenreResponse> genres = genreService.getAll();

            req.setAttribute("movies", movies);
            req.setAttribute("currentPage", movieFilterRequest.page());
            req.setAttribute("typeNumber", movieFilterRequest.typeNumber());
            req.setAttribute("genres", genres);

            log.info("Movies fetched successfully. Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing request for movies.", e);
            throw new ServletException("Error fetching movies", e);
        }
    }
}
