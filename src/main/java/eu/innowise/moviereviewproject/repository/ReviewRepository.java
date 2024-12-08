package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Review;

import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
}
