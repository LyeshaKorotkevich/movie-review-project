package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Review;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
    List<Review> findAll(int page, UUID movieId);

    Optional<Review> findByUserIdAndMovieId(UUID userId, UUID movieId);
}