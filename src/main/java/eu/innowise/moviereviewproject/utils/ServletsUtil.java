package eu.innowise.moviereviewproject.utils;

import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.dto.request.MovieFilterRequest;
import eu.innowise.moviereviewproject.dto.request.RegistrationRequest;
import eu.innowise.moviereviewproject.dto.request.ReviewRequest;
import eu.innowise.moviereviewproject.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public final class ServletsUtil {

    private ServletsUtil() {
    }

    public static RegistrationRequest getRegistrationRequest(HttpServletRequest req) {
        return new RegistrationRequest(
                req.getParameter("username"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("confirmPassword")
        );
    }

    public static ComplaintRequest getComplaintRequest(HttpServletRequest req) {
        UUID userId = UUID.fromString(req.getParameter("userId"));
        UUID reviewId = UUID.fromString(req.getParameter("reviewId"));
        String reason = req.getParameter("reason");

        return new ComplaintRequest(userId, reviewId, reason);
    }

    public static ReviewRequest getReviewRequest(HttpServletRequest req) {
        UserResponse user = (UserResponse) req.getSession().getAttribute("user");
        UUID movieId = UUID.fromString(req.getParameter("movieId"));

        Integer rating = parseInteger(req.getParameter("rating"), null, 1, 10);
        String review = req.getParameter("review");

        return new ReviewRequest(review, rating, user.id(), movieId);
    }

    public static MovieFilterRequest getMovieFilterRequest(HttpServletRequest req) {
        return new MovieFilterRequest(
                parseInteger(req.getParameter("page"), 1, 1, Integer.MAX_VALUE),
                parseInteger(req.getParameter("typeNumber"), 0, 0, 5),
                req.getParameter("genre"),
                parseInteger(req.getParameter("startYear"), null, 1895, 2100),
                parseInteger(req.getParameter("endYear"), null, 1895, 2100),
                parseInteger(req.getParameter("minRating"), null, 1, 10),
                parseInteger(req.getParameter("maxRating"), null, 1, 10)
        );
    }

    public static UUID extractUuidFromPath(HttpServletRequest req, HttpServletResponse resp, int urlLength, int idPosition) {
        String pathInfo = req.getPathInfo();
        log.info("pathInfo to extract UUID: {}", pathInfo);

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != urlLength) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("UUID is required");
            return null;
        }

        UUID id;
        try {
            id = UUID.fromString(pathParts[idPosition - 1]);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("UUID is not a valid UUID");
            return null;
        }
        return id;
    }

    public static Integer parseInteger(String param, Integer defaultValue, int min, int max) {
        try {
            int value = Integer.parseInt(param);
            return Math.max(min, Math.min(max, value));
        } catch (NumberFormatException e) {
            log.warn("Invalid integer value: {}. Using default: {}", param, defaultValue);
            return defaultValue;
        }
    }
}
