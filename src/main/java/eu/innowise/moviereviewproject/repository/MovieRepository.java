package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Movie;

public interface MovieRepository extends Repository<Movie>{

    boolean existsByExternalId(Long externalId);
}
