package eu.innowise.moviereviewproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Movie {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "description")
    private String description;

    @Column(name = "rate_sum", nullable = false)
    private int rateSum;

    @Column(name = "rate_count", nullable = false)
    private int rateCount;

    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    public double getRating() {
        return rateCount == 0 ? 0 : (double) rateSum / rateCount;
    }

    public void addRating(int rating) {
        this.rateSum += rating;
        this.rateCount++;
    }

    public void removeRating(int rating) {
        this.rateSum -= rating;
        this.rateCount--;
    }
}