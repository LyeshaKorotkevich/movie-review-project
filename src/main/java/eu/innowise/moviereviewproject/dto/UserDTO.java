package eu.innowise.moviereviewproject.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String userRole,
        LocalDate createdAt
) {
}
