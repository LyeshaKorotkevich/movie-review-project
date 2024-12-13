package eu.innowise.moviereviewproject.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        String content,
        int rate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserResponse user
) {
}
