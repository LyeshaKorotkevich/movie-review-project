package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.REVIEW_PAGE_SIZE;
import static eu.innowise.moviereviewproject.utils.Constants.SELECT_REVIEWS;

@Slf4j
public class ReviewRepositoryImpl implements ReviewRepository {

    @Override
    public Review save(Review entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(Review entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<Review> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(Review.class, id));
        } catch (Exception e) {
            log.error("Error occurred while finding review by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding review by ID", e);
        }
    }

    @Override
    public List<Review> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery(SELECT_REVIEWS, Review.class).getResultList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all review", e);
            throw new RuntimeException("Error occurred while fetching all reviews", e);
        }
    }

    @Override
    public List<Review> findAll(int page, UUID movieId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            int firstResult = (page - 1) * REVIEW_PAGE_SIZE;
            return entityManager.createQuery("SELECT r FROM Review r WHERE r.movie.id = :movieId", Review.class)
                    .setParameter("movieId", movieId)
                    .setFirstResult(firstResult)
                    .setMaxResults(REVIEW_PAGE_SIZE)
                    .getResultList();
        }
    }

    @Override
    public Optional<Review> findByUserIdAndMovieId(UUID userId, UUID movieId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT r FROM Review r WHERE r.user.id = :userId AND r.movie.id = :movieId";
            Review review = entityManager.createQuery(jpql, Review.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResultOrNull();
            return Optional.ofNullable(review);
        }
    }

    @Override
    public List<Review> findByUserId(UUID userId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT r FROM Review r LEFT JOIN FETCH r.movie WHERE r.user.id = :userId";
            return entityManager.createQuery(jpql, Review.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            Review review = entityManager.find(Review.class, id);
            if (review != null) {
                entityManager.remove(review);
                log.info("Review deleted successfully with ID: {}", id);
            } else {
                log.error("Review with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}
