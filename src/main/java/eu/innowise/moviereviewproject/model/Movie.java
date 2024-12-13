package eu.innowise.moviereviewproject.model;

import eu.innowise.moviereviewproject.model.enums.MovieType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_id", unique = true, nullable = false)
    private Long externalId;

    @Column(nullable = false)
    private String title;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "description")
    private String description;

    @Column(name = "rate_sum", nullable = false)
    private int rateSum;

    @Column(name = "rate_count", nullable = false)
    private int rateCount;

    @Column(nullable = false)
    private Double rating;

    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_person",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> persons = new HashSet<>();

    @Column(name = "movie_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieType movieType;
}