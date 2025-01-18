package integration;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.impl.GenreRepositoryImpl;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenreRepositoryImplIntegrationTest {

    private GenreRepositoryImpl genreRepository;

    @BeforeAll
    public static void setUpDatabase() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        entityManager.getTransaction().begin();

        Genre genre1 = new Genre();
        genre1.setName("Action");
        entityManager.persist(genre1);

        Genre genre2 = new Genre();
        genre2.setName("Comedy");
        entityManager.persist(genre2);

        Genre genre3 = new Genre();
        genre3.setName("Drama");
        entityManager.persist(genre3);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @BeforeEach
    void setUp() {
        genreRepository = GenreRepositoryImpl.getInstance();
    }

    @Test
    void testFindByName() {
        Genre result = genreRepository.findByName("Action").orElse(null);

        assertNotNull(result);
        assertEquals("Action", result.getName());
    }

    @Test
    void testFindAll() {
        assertEquals(3, genreRepository.findAll().size());
    }

    @Test
    void testFindByNameNotFound() {
        assertTrue(genreRepository.findByName("Nonexistent Genre").isEmpty());
    }
}
