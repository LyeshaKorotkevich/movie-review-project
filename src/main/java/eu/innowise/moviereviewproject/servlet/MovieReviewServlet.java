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
import java.util.UUID;

@Slf4j
@WebServlet("/movies/*/review")
public class MovieReviewServlet extends HttpServlet {

    private final MovieService movieService;
    //private final ReviewService reviewService;

    public MovieReviewServlet() {
        this.movieService = ApplicationConfig.getMovieService();
        //this.reviewService = ApplicationConfig.getReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UUID movieId = ServletsUtil.extractUuidFromPath(req, resp, 2, 1);

            String ratingString = req.getParameter("rating");
            String reviewText = req.getParameter("review");

            //int rating = Integer.parseInt(ratingString);

            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {

            }
            // Create Review

            //reviewService.saveReview(review);

            log.info("Successfully added review for movie: {} by user", movie.getTitle());

            // forward to /movie/{id}

        } catch (Exception e) {
            log.error("Error occurred while processing review for movie.", e);
        }
    }
}
