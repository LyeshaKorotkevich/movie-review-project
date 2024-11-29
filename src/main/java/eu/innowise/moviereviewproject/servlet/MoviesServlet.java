package eu.innowise.moviereviewproject.servlet;

import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.service.ApiService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {

    private final ApiService apiService = new ApiService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int page = 1;
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    // TODO logging
                }
            }

            // TODO maybe to do async
            List<Movie> movies = apiService.fetchMoviesFromApi(page);

            req.setAttribute("movies", movies);
            req.setAttribute("currentPage", page);

            req.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Ошибка получения фильмов", e);
        }
    }

}
