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
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", entity.getEmail(), e);
            throw new RuntimeException("Error occurred while saving the user", e);
        }
        return entity;
    }

    @Override
    public void update(User entity) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while updating user: {}", entity.getEmail(), e);
            throw new RuntimeException("Error occurred while updating the user", e);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT u FROM User u WHERE u.id = :id";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
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
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                log.info("User deleted successfully with ID: {}", id);
            } else {
                log.warn("User with ID: {} not found for deletion", id);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while deleting user with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting the user", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT 1 FROM User u WHERE u.username = :username";
            List result = entityManager.createQuery(jpql)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .getResultList();
            return !result.isEmpty();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT 1 FROM User u WHERE u.email = :email";
            List result = entityManager.createQuery(jpql)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getResultList();
            return !result.isEmpty();
        }
    }
}
