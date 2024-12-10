package eu.innowise.moviereviewproject.mapper;

import eu.innowise.moviereviewproject.dto.request.ReviewRequest;
import eu.innowise.moviereviewproject.dto.response.ReviewResponse;
import eu.innowise.moviereviewproject.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toReview(ReviewRequest reviewRequest);

    ReviewResponse toResponse(Review review);
}
