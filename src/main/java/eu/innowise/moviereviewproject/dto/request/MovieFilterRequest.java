package eu.innowise.moviereviewproject.dto.request;

public record MovieFilterRequest(
        Integer page,
        Integer typeNumber,
        String genre,
        Integer startYear,
        Integer endYear,
        Integer minRating,
        Integer maxRating
) {
}
