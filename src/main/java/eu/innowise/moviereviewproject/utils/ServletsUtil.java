package eu.innowise.moviereviewproject.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class ServletsUtil {

    private ServletsUtil() {
    }

    public static UUID extractUuidFromPath(HttpServletRequest req, HttpServletResponse resp, int urlLength, int idPosition) throws IOException {
        String pathInfo = req.getPathInfo();

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != urlLength) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("UUID is required");
            return null;
        }

        UUID id;
        try {
            id = UUID.fromString(pathParts[idPosition]);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("UUID is not a valid UUID");
            return null;
        }
        return id;
    }
}
