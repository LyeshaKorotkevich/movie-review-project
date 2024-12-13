package eu.innowise.moviereviewproject.dto.response;

import java.util.UUID;

public record ComplaintResponse(
        UUID id,
        String reason,
        String status,
        String userName,
        String reviewContent
) {
}
