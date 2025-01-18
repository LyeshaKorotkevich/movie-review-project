package integration;

import eu.innowise.moviereviewproject.dto.request.MovieFilterRequest;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.enums.MovieType;
import eu.innowise.moviereviewproject.repository.impl.MovieRepositoryImpl;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieRepositoryImplIntegrationTest {

    private static MovieRepositoryImpl movieRepository;

    @BeforeAll
    public static void setUpDatabase() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        entityManager.getTransaction().begin();

        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setExternalId(101L);
        movie1.setReleaseYear(2020);
        movie1.setRating(8.5);
        movie1.setMovieType(MovieType.MOVIE);
        entityManager.persist(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Another Movie");
        movie2.setExternalId(102L);
        movie2.setReleaseYear(2018);
        movie2.setRating(7.2);
        movie2.setMovieType(MovieType.ANIME);
        entityManager.persist(movie2);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @BeforeEach
    public void setUp() {
        movieRepository = MovieRepositoryImpl.getInstance();
    }

    @Test
    void testFindByExternalId() {
        Optional<Movie> result = movieRepository.findByExternalId(101L);
        assertTrue(result.isPresent());
        assertEquals(101L, result.get().getExternalId());
    }

    @Test
    void testFindAll() {
        List<Movie> result = movieRepository.findAll(1, MovieType.ANIME);
        assertEquals(1, result.size());
    }

    @Test
    void testFindMoviesByPartialTitle() {
        List<Movie> result = movieRepository.findMoviesByPartialTitle("Movie", 1);
        assertEquals(2, result.size());
    }

    @Test
    void testFindFilteredMovies() {
        MovieFilterRequest filterRequest = new MovieFilterRequest(
                1,
                0,
                null,
                2018,
                2019,
                1,
                9
        );

        List<Movie> result = movieRepository.findFilteredMovies(filterRequest);
        assertEquals(1, result.size());
        assertEquals("Another Movie", result.get(0).getTitle());
    }

    @Test
    void testExistsByExternalId() {
        assertTrue(movieRepository.existsByExternalId(101L));
        assertFalse(movieRepository.existsByExternalId(999L));
    }
}
