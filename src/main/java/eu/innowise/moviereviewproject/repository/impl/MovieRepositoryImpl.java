package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Genre;
import eu.innowise.moviereviewproject.model.Movie;
import eu.innowise.moviereviewproject.model.MovieType;
import eu.innowise.moviereviewproject.repository.MovieRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.MOVIE_PAGE_SIZE;

@Slf4j
public class MovieRepositoryImpl implements MovieRepository {

    @Override
    public Movie save(Movie movie) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(movie);
            return movie;
        });
    }

    @Override
    public void update(Movie movie) {
        executeInTransaction(entityManager -> {
            entityManager.merge(movie);
            return null;
        });
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT m FROM Movie m LEFT JOIN FETCH m.persons LEFT JOIN FETCH m.genres WHERE m.id = :id";
            Movie movie = entityManager.createQuery(jpql, Movie.class)
                    .setParameter("id", id)
                    //.setHint("org.hibernate.cacheable", true)
                    .getSingleResultOrNull();
            return Optional.ofNullable(movie);
        } catch (Exception e) {
            log.error("Error occurred while finding movie by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding movie", e);
        }
    }

    @Override
    public Optional<Movie> findByExternalId(Long externalId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT m FROM Movie m WHERE m.externalId = :externalId";
            Movie movie = entityManager.createQuery(jpql, Movie.class)
                    .setParameter("externalId", externalId)
                    .getSingleResultOrNull();
            return Optional.ofNullable(movie);
        } catch (Exception e) {
            log.error("Error occurred while finding movie by externalId: {}", externalId, e);
            throw new RuntimeException("Error occurred while finding movie", e);
        }
    }

    @Override
    public List<Movie> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        }
    }

    @Override
    public List<Movie> findAll(int page, int typeNumber) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            int firstResult = (page - 1) * MOVIE_PAGE_SIZE;
            MovieType movieType = MovieType.fromTypeNumber(typeNumber);
            return entityManager.createQuery("SELECT m FROM Movie m WHERE m.movieType = :movieType", Movie.class)
                    .setParameter("movieType", movieType)
                    .setFirstResult(firstResult)
                    .setMaxResults(MOVIE_PAGE_SIZE)
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
        }
    }

    @Override
    public List<Movie> findMoviesByPartialTitle(String query, int page) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            int firstResult = (page - 1) * MOVIE_PAGE_SIZE;
            String jpql = "SELECT m FROM Movie m WHERE m.title ILIKE :query";
            return entityManager.createQuery(jpql, Movie.class)
                    .setParameter("query", "%" + query + "%")
                    .setFirstResult(firstResult)
                    .setMaxResults(MOVIE_PAGE_SIZE)
                    .getResultList();
        }
    }

    @Override
    public List<Movie> findFilteredMovies(int page, Integer typeNumber, String genre, Integer startYear, Integer endYear, Integer minRating, Integer maxRating) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> movie = cq.from(Movie.class);
            Join<Movie, Genre> genreJoin = movie.join("genres");

            List<Predicate> predicates = new ArrayList<>();
            if (genre != null && !genre.isEmpty()) {
                predicates.add(cb.equal(genreJoin.get("name"), genre));
            }
            if (typeNumber != null && typeNumber != 0) {
                predicates.add(cb.equal(movie.get("movieType"), MovieType.fromTypeNumber(typeNumber)));
            }
            if (startYear != null) {
                predicates.add(cb.greaterThanOrEqualTo(movie.get("releaseYear"), startYear));
            }
            if (endYear != null) {
                predicates.add(cb.lessThanOrEqualTo(movie.get("releaseYear"), endYear));
            }
            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(movie.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicates.add(cb.lessThanOrEqualTo(movie.get("rating"), maxRating));
            }

            cq.select(movie).distinct(true).where(predicates.toArray(new Predicate[0]));

            TypedQuery<Movie> query = entityManager.createQuery(cq);
            query.setFirstResult((page - 1) * MOVIE_PAGE_SIZE);
            query.setMaxResults(MOVIE_PAGE_SIZE);

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
        executeInTransaction(entityManager -> {
            Movie movie = entityManager.find(Movie.class, id);
            if (movie != null) {
                entityManager.remove(movie);
                log.info("Movie deleted successfully with ID: {}", id);
            } else {
                log.warn("Movie with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}