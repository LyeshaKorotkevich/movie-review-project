package eu.innowise.moviereviewproject.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchlistResponse(
        UUID id,
        MovieResponse movie,
        LocalDateTime addedAt,
        boolean isWatched
) {
}
