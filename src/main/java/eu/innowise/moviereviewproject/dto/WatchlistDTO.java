package eu.innowise.moviereviewproject.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchlistDTO(UUID id, UserDTO user, MovieDTO movie, LocalDateTime addedAt, boolean isWatched  ) {
}
