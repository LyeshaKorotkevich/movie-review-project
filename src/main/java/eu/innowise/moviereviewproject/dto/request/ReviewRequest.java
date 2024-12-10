package eu.innowise.moviereviewproject.dto.request;

import java.util.UUID;

public record ReviewRequest(
        String content,
        int rate,
        UUID userId,
        UUID movieId
) {
}
