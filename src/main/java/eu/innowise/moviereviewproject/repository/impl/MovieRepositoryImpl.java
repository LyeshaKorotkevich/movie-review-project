package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.MovieType;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MovieRepositoryImpl implements MovieRepository {

    private static final int PAGE_SIZE = 12;

    // TODO maybe it is right to do rollback if error occurs

    @Override
    public Movie save(Movie movie) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while saving movie: {}", movie.getTitle(), e);
            throw new RuntimeException("Error occurred while saving the movie", e);
        }
        return movie;
    }

    @Override
    public void update(Movie movie) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while updating movie: {}", movie.getTitle(), e);
            throw new RuntimeException("Error occurred while updating the movie", e);
        }
    }

    public Optional<Movie> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT m FROM Movie m LEFT JOIN FETCH m.persons LEFT JOIN FETCH m.genres WHERE m.id = :id";
            Movie movie = entityManager.createQuery(jpql, Movie.class)
                    .setParameter("id", id)
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
            return Optional.ofNullable(movie);
        }
    }

    public List<Movie> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        }
    }

    public List<Movie> findAll(int page, int typeNumber) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            int firstResult = (page - 1) * PAGE_SIZE;
            MovieType movieType = MovieType.fromTypeNumber(typeNumber);
            return entityManager.createQuery("SELECT m FROM Movie m WHERE m.movieType = :movieType", Movie.class)
                    .setParameter("movieType", movieType)
                    .setFirstResult(firstResult)
                    .setMaxResults(PAGE_SIZE)
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
        }
    }

    @Override
    public List<Movie> findMoviesByPartialTitle(String query, int page) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            int firstResult = (page - 1) * PAGE_SIZE;
            String jpql = "SELECT m FROM Movie m WHERE m.title ILIKE :query";
            return entityManager.createQuery(jpql, Movie.class)
                    .setParameter("query", "%" + query + "%")
                    .setFirstResult(firstResult)
                    .setMaxResults(PAGE_SIZE)
                    .getResultList();
        }
    }

    @Override
    public List<Movie> findFilteredMovies(int page, Integer typeNumber, String genre, Integer startYear, Integer endYear, Integer minRating, Integer maxRating) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            StringBuilder queryBuilder = new StringBuilder("SELECT m FROM Movie m WHERE 1=1");

            if (typeNumber!= null && typeNumber != 0) {
                queryBuilder.append(" AND m.movieType = :movieType");
            }
            if (genre != null && !genre.isEmpty()) {
                queryBuilder.append(" AND m.genre = :genre");
            }
            if (startYear != null) {
                queryBuilder.append(" AND m.releaseYear >= :startYear");
            }
            if (endYear != null) {
                queryBuilder.append(" AND m.releaseYear <= :endYear");
            }
            if (minRating != null) {
                queryBuilder.append(" AND m.rating >= :minRating");
            }
            if (maxRating != null) {
                queryBuilder.append(" AND m.rating <= :maxRating");
            }

            Query query = entityManager.createQuery(queryBuilder.toString(), Movie.class);

            if (typeNumber!= null && typeNumber != 0) {
                MovieType movieType = MovieType.fromTypeNumber(typeNumber);
                query.setParameter("movieType", movieType);
            }
            if (genre != null && !genre.isEmpty()) {
                query.setParameter("genre", genre);
            }
            if (startYear != null) {
                query.setParameter("startYear", startYear);
            }
            if (endYear != null) {
                query.setParameter("endYear", endYear);
            }
            if (minRating != null) {
                query.setParameter("minRating", minRating);
            }
            if (maxRating != null) {
                query.setParameter("maxRating", maxRating);
            }

            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);

            return query.getResultList();
        }
    }

    @Override
    public boolean existsByExternalId(Long externalId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT 1 FROM Movie m WHERE m.externalId = :externalId";
            List result = entityManager.createQuery(jpql)
                    .setParameter("externalId", externalId)
                    .setMaxResults(1)
                    .getResultList();
            return !result.isEmpty();
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Movie movie = entityManager.find(Movie.class, id);
            if (movie != null) {
                entityManager.remove(movie);
                log.info("Movie deleted successfully with ID: {}", id);
            } else {
                log.warn("Movie with ID: {} not found for deletion", id);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while deleting movie with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting the movie", e);
        }
    }
}
