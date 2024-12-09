package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovieMapper {

    @Mapping(target = "rating", expression = "java(movie.getRating())")
    MovieResponse toDetailedResponse(Movie movie);

    @Mapping(target = "rating", expression = "java(movie.getRating())")
    @Mapping(target = "persons", ignore = true)
    @Mapping(target = "genres", ignore = true)
    MovieResponse toSummaryResponse(Movie movie);
}