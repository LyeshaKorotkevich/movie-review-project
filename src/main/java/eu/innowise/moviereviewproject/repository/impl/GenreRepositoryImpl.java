package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class GenreRepositoryImpl implements GenreRepository {

    @Override
    public Optional<Genre> findByName(String name) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery(
                            "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            log.error("Error occurred while fetching genre by name: {}", name, e);
            throw new RuntimeException("Error occurred while fetching genre by name", e);
        }
    }

    @Override
    public List<Genre> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        }
    }

    @Override
    public Genre saveIfNotExists(String name) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();

            Optional<Genre> existingGenre = findByName(name);

            Genre genre;
            if (existingGenre.isPresent()) {
                genre = existingGenre.get();
            } else {
                genre = new Genre();
                genre.setName(name);
                entityManager.persist(genre);
                log.info("New genre saved: {}", genre.getName());
            }

            entityManager.getTransaction().commit();
            return genre;
        } catch (Exception e) {
            log.error("Error occurred while saving genre: {}", name, e);
            throw new RuntimeException("Error occurred while saving genre", e);
        }
    }
}
