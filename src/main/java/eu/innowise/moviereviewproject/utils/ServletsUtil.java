package eu.innowise.moviereviewproject.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public final class ServletsUtil {

    private ServletsUtil() {
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
