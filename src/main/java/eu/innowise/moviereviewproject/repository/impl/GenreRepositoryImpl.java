package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class GenreRepositoryImpl implements GenreRepository {

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
                            "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
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
            return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
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
