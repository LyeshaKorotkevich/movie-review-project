package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static eu.innowise.moviereviewproject.utils.Constants.SELECT_GENRES;
import static eu.innowise.moviereviewproject.utils.Constants.SELECT_GENRES_BY_NAME;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreRepositoryImpl implements GenreRepository {

    private static class SingletonHelper {
        private static final GenreRepositoryImpl INSTANCE = new GenreRepositoryImpl();
    }

    public static GenreRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Genre save(Genre genre) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(genre);
            return genre;
        });
    }

    @Override
    public void update(Genre genre) {
        executeInTransaction(entityManager -> {
            entityManager.merge(genre);
            return null;
        });
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(Genre.class, id));
        } catch (Exception e) {
            log.error("Error occurred while finding genre by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding genre by ID", e);
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.createQuery(
                            SELECT_GENRES_BY_NAME, Genre.class)
                    .setParameter("name", name)
                    .getSingleResultOrNull()
            );

        } catch (Exception e) {
            log.error("Error occurred while fetching genre by name: {}", name, e);
            throw new RuntimeException("Error occurred while fetching genre by name", e);
        }
    }

    @Override
    public List<Genre> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery(SELECT_GENRES, Genre.class).getResultList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all genres", e);
            throw new RuntimeException("Error occurred while fetching all genres", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        executeInTransaction(entityManager -> {
            Genre genre = entityManager.find(Genre.class, id);
            if (genre != null) {
                entityManager.remove(genre);
                log.info("Genre deleted successfully with ID: {}", id);
            } else {
                log.warn("Genre with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}
