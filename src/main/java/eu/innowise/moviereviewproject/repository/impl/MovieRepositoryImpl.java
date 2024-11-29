package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieRepositoryImpl implements MovieRepository {

    private final EntityManager entityManager = JpaUtil.getEntityManager();

    @Override
    public void save(Movie movie) {
        entityManager.getTransaction().begin();
        entityManager.persist(movie);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Movie movie) {
        entityManager.getTransaction().begin();
        entityManager.merge(movie);
        entityManager.getTransaction().commit();
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        Movie movie = entityManager.find(Movie.class, id);
        return Optional.ofNullable(movie);
    }

    public List<Movie> findAll() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    @Override
    public boolean existsByExternalId(Long externalId) {
        String jpql = "SELECT 1 FROM Movie m WHERE m.externalId = :externalId";
        List result = entityManager.createQuery(jpql)
                .setParameter("externalId", externalId)
                .setMaxResults(1)
                .getResultList();
        return !result.isEmpty();
    }

    @Override
    public void deleteById(UUID id) {
        entityManager.getTransaction().begin();
        Movie movie = entityManager.find(Movie.class, id);
        if (movie != null) {
            entityManager.remove(movie);
        }
        entityManager.getTransaction().commit();
    }
}
