package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Watchlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchlistRepository extends CrudRepository<Watchlist, UUID> {

    Optional<Watchlist> findByUserIdAndMovieId(UUID userId, UUID movieId);

    List<Watchlist> findByUserId(int page, UUID userId);
}
