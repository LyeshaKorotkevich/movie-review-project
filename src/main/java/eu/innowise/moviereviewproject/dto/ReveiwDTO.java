package eu.innowise.moviereviewproject.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReveiwDTO(UUID id, String content, int rate,
         LocalDateTime createdAt, LocalDateTime updatedAt, UserDTO user, MovieDTO movie) {
}
