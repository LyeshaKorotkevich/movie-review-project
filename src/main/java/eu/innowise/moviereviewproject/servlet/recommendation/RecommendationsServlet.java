package eu.innowise.moviereviewproject.servlet.recommendation;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import eu.innowise.moviereviewproject.service.RecommendationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/recommendations")
public class RecommendationsServlet extends HttpServlet {

    private RecommendationService recommendationService;

    @Override
    public void init() throws ServletException {
        this.recommendationService = ApplicationConfig.getRecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserResponse userResponse = (UserResponse) req.getSession().getAttribute("user");
            List<MovieResponse> recommendations = recommendationService.recommendForUser(userResponse);
            log.info(String.valueOf(recommendations.size()));

            req.setAttribute("recommendations", recommendations);
            req.getRequestDispatcher("/WEB-INF/views/recommendations.jsp").forward(req, resp);

        } catch (Exception e) {
            log.error("Error during recommendation process.");
        }
    }
}
