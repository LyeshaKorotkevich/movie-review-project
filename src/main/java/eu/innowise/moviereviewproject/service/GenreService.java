package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.response.GenreResponse;
import eu.innowise.moviereviewproject.mapper.GenreMapper;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
        this.genreMapper = Mappers.getMapper(GenreMapper.class);
    }

    public List<GenreResponse> getAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toResponse)
                .toList();
    }
}
