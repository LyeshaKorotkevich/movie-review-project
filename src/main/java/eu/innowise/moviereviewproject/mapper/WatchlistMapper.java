package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.response.WatchlistResponse;
import eu.innowise.moviereviewproject.model.Watchlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = MovieMapper.class)
public interface WatchlistMapper {

    @Mapping(target = "movie", source = "watchlist.movie", qualifiedByName = "toSummaryResponse")
    WatchlistResponse toResponse(Watchlist watchlist);
}
