package eu.innowise.moviereviewproject.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String userRole,
        LocalDate createdAt
) {
}
