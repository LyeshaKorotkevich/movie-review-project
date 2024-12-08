package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.GenreDTO;
import eu.innowise.moviereviewproject.model.Genre;
import org.mapstruct.Mapper;

@Mapper
public interface GenreMapper {

    GenreDTO toDTO(Genre genre);
}
