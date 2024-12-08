package eu.innowise.moviereviewproject.dto.response;

import eu.innowise.moviereviewproject.dto.MovieDTO;
import eu.innowise.moviereviewproject.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        String content,
        int rate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserDTO user,
        MovieDTO movie
) {
}
