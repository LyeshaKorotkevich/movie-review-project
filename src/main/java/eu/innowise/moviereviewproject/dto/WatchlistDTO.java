package eu.innowise.moviereviewproject.dto;

import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.dto.response.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchlistDTO(UUID id, UserResponse user, MovieResponse movie, LocalDateTime addedAt, boolean isWatched  ) {
}
