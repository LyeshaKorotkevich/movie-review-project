package eu.innowise.moviereviewproject.servlet.movie;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.MovieDTO;
import eu.innowise.moviereviewproject.dto.PersonDTO;
import eu.innowise.moviereviewproject.service.MovieService;
import eu.innowise.moviereviewproject.utils.ServletsUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

            MovieDTO movie = movieService.getMovieById(movieId);

            Map<String, List<PersonDTO>> filteredPersons = filterAndLimitPersons(movie);

            req.setAttribute("movie", movie);
            req.setAttribute("filteredPersons", filteredPersons);

            log.info("Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movie-details.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing request for movies.", e);
            throw new ServletException("Error getting movie", e);
        }
    }

    private Map<String, List<PersonDTO>> filterAndLimitPersons(MovieDTO movie) {
        return movie.persons()
                .stream()
                .filter(person -> isValidName(person.name()) || isValidName(person.enName()))
                .filter(person -> "актеры".equalsIgnoreCase(person.profession()) || "режиссеры".equalsIgnoreCase(person.profession()))
                .collect(Collectors.groupingBy(
                        person -> person.profession(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().limit(4).collect(Collectors.toList())
                        )
                ));
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && !name.equals("null");
    }
}
