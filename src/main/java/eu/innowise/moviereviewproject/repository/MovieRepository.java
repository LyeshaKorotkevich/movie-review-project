package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.dto.request.MovieFilterRequest;
import eu.innowise.moviereviewproject.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends CrudRepository<Movie, UUID> {

    boolean existsByExternalId(Long externalId);

    List<Movie> findAll(int page, int typeNumber);

    List<Movie> findMoviesByPartialTitle(String query, int page);

    List<Movie> findFilteredMovies(MovieFilterRequest movieFilterRequest);

    Optional<Movie> findByExternalId(Long externalId);
}
