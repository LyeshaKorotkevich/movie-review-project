package eu.innowise.moviereviewproject.servlet;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.MovieDTO;
import eu.innowise.moviereviewproject.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static eu.innowise.moviereviewproject.utils.ServletsUtil.parseInteger;

@WebServlet("/movies/search")
public class MovieSearchServlet extends HttpServlet {

    private final MovieService movieService;

    public MovieSearchServlet() {
        this.movieService = ApplicationConfig.getMovieService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        int page = parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE);

        List<MovieDTO> movies = movieService.searchMovies(page, query);

        req.setAttribute("movies", movies);
        req.setAttribute("currentPage", page);
        req.setAttribute("query", query);

        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }
}
