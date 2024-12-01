package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Genre;

import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> findByName(String name);
    Genre saveIfNotExists(String name);
}
