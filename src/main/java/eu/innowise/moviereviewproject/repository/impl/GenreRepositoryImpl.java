package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static eu.innowise.moviereviewproject.utils.Constants.SELECT_GENRES;
import static eu.innowise.moviereviewproject.utils.Constants.SELECT_GENRES_BY_NAME;

@Slf4j
public class GenreRepositoryImpl extends AbstractHibernateDao<Genre, Integer> implements GenreRepository {

    private GenreRepositoryImpl() {
        super(Genre.class);
    }

    private static class SingletonHelper {
        private static final GenreRepositoryImpl INSTANCE = new GenreRepositoryImpl();
    }

    public static GenreRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
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
}
