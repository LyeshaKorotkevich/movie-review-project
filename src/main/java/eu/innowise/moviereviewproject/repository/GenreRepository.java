package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> findByName(String name);
    List<Genre> findAll();
    Genre saveIfNotExists(String name);
}
