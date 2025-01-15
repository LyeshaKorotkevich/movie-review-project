package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.REVIEW_PAGE_SIZE;

@Slf4j
public class ReviewRepositoryImpl extends AbstractHibernateDao<Review, UUID> implements ReviewRepository {

    private ReviewRepositoryImpl() {
        super(Review.class);
    }

    private static class SingletonHelper {
        private static final ReviewRepositoryImpl INSTANCE = new ReviewRepositoryImpl();
    }

    public static ReviewRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
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
}
