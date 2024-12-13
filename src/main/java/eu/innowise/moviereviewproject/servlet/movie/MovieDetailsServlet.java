package eu.innowise.moviereviewproject.servlet.movie;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.dto.response.PersonResponse;
import eu.innowise.moviereviewproject.dto.response.ReviewResponse;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.service.MovieService;
import eu.innowise.moviereviewproject.service.ReviewService;
import eu.innowise.moviereviewproject.service.WatchlistService;
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

import static eu.innowise.moviereviewproject.utils.ServletsUtil.parseInteger;

@Slf4j
@WebServlet("/movies/*")
public class MovieDetailsServlet extends HttpServlet {

    private MovieService movieService;
    private ReviewService reviewService;
    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        this.movieService = ApplicationConfig.getMovieService();
        this.reviewService = ApplicationConfig.getReviewService();
        this.watchlistService = ApplicationConfig.getWatchlistService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int page = parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE);
            UUID movieId = ServletsUtil.extractUuidFromPath(req, resp, 2, 2);

            log.info("Extracted movieId={}", movieId);

            MovieResponse movie = movieService.getMovieById(movieId);
            Map<String, List<PersonResponse>> filteredPersons = filterAndLimitPersons(movie);

            UserResponse user = (UserResponse) req.getSession().getAttribute("user");
            ReviewResponse existingReview = null;
            boolean isInWatchlist = false;
            if (user != null) {
                existingReview = reviewService.getReviewByUserAndMovie(user.id(), movieId);
                isInWatchlist = watchlistService.checkIfWatchlistExists(user.id(), movieId);
            }
            List<ReviewResponse> reviewResponses = reviewService.getAllReviews(page, movieId);

            req.setAttribute("movie", movie);
            req.setAttribute("filteredPersons", filteredPersons);
            req.setAttribute("reviews", reviewResponses);
            req.setAttribute("page", page);
            req.setAttribute("existingReview", existingReview);
            req.setAttribute("isInWatchlist", isInWatchlist);

            log.info("Forwarding to JSP.");

            req.getRequestDispatcher("/WEB-INF/views/movie-details.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error occurred while processing request for movies.", e);
            throw new ServletException("Error getting movie", e);
        }
    }

    private Map<String, List<PersonResponse>> filterAndLimitPersons(MovieResponse movie) {
        return movie.persons()
                .stream()
                .filter(person -> (!person.name().isBlank()) || !person.enName().isBlank())
                .filter(person -> "актеры".equalsIgnoreCase(person.profession()) || "режиссеры".equalsIgnoreCase(person.profession()))
                .collect(Collectors.groupingBy(
                        PersonResponse::profession,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().limit(4).toList()
                        )
                ));
    }
}
