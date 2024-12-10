package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.response.MovieResponse;
import eu.innowise.moviereviewproject.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface MovieMapper {

    MovieResponse toDetailedResponse(Movie movie);

    @Named("toSummaryResponse")
    @Mapping(target = "persons", ignore = true)
    @Mapping(target = "genres", ignore = true)
    MovieResponse toSummaryResponse(Movie movie);
}