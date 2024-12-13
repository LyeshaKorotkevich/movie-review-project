package eu.innowise.moviereviewproject.dto.request;

import java.util.UUID;

public record ComplaintRequest(
        UUID userId,
        UUID reviewId,
        String reason
) {
}
