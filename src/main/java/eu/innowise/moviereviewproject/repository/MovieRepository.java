package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Movie;

import java.util.List;

public interface MovieRepository extends Repository<Movie>{

    boolean existsByExternalId(Long externalId);

    List<Movie> findAll(int page, int typeNumber);
}
