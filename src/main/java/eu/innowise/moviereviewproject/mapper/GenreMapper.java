package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.response.GenreResponse;
import eu.innowise.moviereviewproject.model.Genre;
import org.mapstruct.Mapper;

@Mapper
public interface GenreMapper {

    GenreResponse toResponse(Genre genre);
}
