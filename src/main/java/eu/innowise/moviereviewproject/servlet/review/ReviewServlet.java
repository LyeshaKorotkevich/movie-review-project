package eu.innowise.moviereviewproject.servlet.review;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.dto.request.ReviewRequest;
import eu.innowise.moviereviewproject.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.ServletsUtil.parseInteger;

@Slf4j
@WebServlet(value = "/review")
public class ReviewServlet extends HttpServlet{

    private final ReviewService reviewService;

    public ReviewServlet() {
        this.reviewService = ApplicationConfig.getReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserResponse user = (UserResponse) req.getSession().getAttribute("user");
            UUID movieId = UUID.fromString(req.getParameter("movieId"));
            log.info("Received movieId for update: {}", movieId);

            Integer rating = parseInteger(req.getParameter("rating"), null, 1, 10);
            String review = req.getParameter("review");

            ReviewRequest reviewRequest = new ReviewRequest(review, rating, user.id(), movieId);

            String reviewIdParam = req.getParameter("reviewId");
            if (reviewIdParam == null) {
                log.info("Saving new review");
                reviewService.saveReview(reviewRequest);
            } else {
                log.info("Updating review");
                UUID reviewId = UUID.fromString(reviewIdParam);
                reviewService.updateReview(reviewRequest, reviewId);
            }

            log.info("Successfully added review for movie with id: {} by user", movieId);

            resp.sendRedirect(req.getContextPath() + "/movies/" + movieId);
        } catch (Exception e) {
            log.error("Error occurred while processing review for movie.", e);
        }
    }
}
