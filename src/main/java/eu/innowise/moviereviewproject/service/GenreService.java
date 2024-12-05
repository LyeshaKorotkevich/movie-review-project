package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.GenreRepository;

import java.util.List;

public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
