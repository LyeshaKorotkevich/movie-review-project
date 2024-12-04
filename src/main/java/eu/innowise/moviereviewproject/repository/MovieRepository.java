package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Movie;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie> {

    boolean existsByExternalId(Long externalId);

    List<Movie> findAll(int page, int typeNumber);

    List<Movie> findMoviesByPartialTitle(String query, int page);

    List<Movie> findFilteredMovies(int page, Integer typeNumber, String genre, Integer startYear, Integer endYear, Integer minRating, Integer maxRating);
}
