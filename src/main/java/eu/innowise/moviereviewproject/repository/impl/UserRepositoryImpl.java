package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.repository.UserRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(User entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<User> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT u FROM User u WHERE u.id = :id";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .getSingleResultOrNull();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                log.info("User deleted successfully with ID: {}", id);
            } else {
                log.warn("User with ID: {} not found for deletion", id);
            }
            return null;
        });
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
