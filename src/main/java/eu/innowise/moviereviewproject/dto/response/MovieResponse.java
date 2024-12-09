package eu.innowise.moviereviewproject.dto.response;

import java.util.Set;
import java.util.UUID;

public record MovieResponse(
        UUID id,
        String title,
        String posterUrl,
        int releaseYear,
        String description,
        double rating,
        Set<GenreResponse>genres,
        Set<PersonResponse> persons ) {
}
