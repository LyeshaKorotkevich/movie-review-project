package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.repository.GenreRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class GenreRepositoryImpl implements GenreRepository {

    private final EntityManager entityManager = JpaUtil.getEntityManager();


    @Override
    public Optional<Genre> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Genre saveIfNotExists(String name) {
        entityManager.getTransaction().begin();

        Optional<Genre> existingGenre = findByName(name);

        Genre genre;
        if (existingGenre.isPresent()) {
            genre = existingGenre.get();
        } else {
            genre = new Genre();
            genre.setName(name);
            entityManager.persist(genre);
        }

        entityManager.getTransaction().commit();
        return genre;
    }
}
