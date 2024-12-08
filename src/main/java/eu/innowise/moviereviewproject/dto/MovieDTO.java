package eu.innowise.moviereviewproject.dto;

import java.util.Set;
import java.util.UUID;

public record MovieDTO(
        UUID id,
        String title,
        String posterUrl,
         int releaseYear, String description,
        double rating,
        Set<GenreDTO>genres,
        Set<PersonDTO> persons ) {
}
