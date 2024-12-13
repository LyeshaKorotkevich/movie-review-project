package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Integer> {

    Optional<Genre> findByName(String name);
}
