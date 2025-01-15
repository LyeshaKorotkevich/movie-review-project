package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.exceptions.EntityAlreadyExistsException;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.SELECT_USERS;
import static eu.innowise.moviereviewproject.utils.Constants.SELECT_USER_BY_USERNAME;

@Slf4j
public class UserRepositoryImpl extends AbstractHibernateDao<User, UUID> implements UserRepository {

    private UserRepositoryImpl() {
        super(User.class);
    }

    private static class SingletonHelper {
        private static final UserRepositoryImpl INSTANCE = new UserRepositoryImpl();
    }

    public static UserRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public User save(User entity) {
        try {
            return executeInTransactionWithReturn(entityManager -> {
                entityManager.persist(entity);
                log.info("Entity saved: {}", entity);
                return entity;
            });
        } catch (PersistenceException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                log.error("Constraint violation while saving user: {}", e.getMessage());
                throw new EntityAlreadyExistsException("User with this username or email already exists", e);
            }
            throw e;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            User user = entityManager.createQuery(SELECT_USER_BY_USERNAME, User.class)
                    .setParameter("username", username)
                    .getSingleResultOrNull();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery(SELECT_USERS, User.class).getResultList();
        }
    }

    @Override
    public boolean existsById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT 1 FROM User u WHERE u.id = :id";
            List result = entityManager.createQuery(jpql)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getResultList();
            return !result.isEmpty();
        }
    }
}
