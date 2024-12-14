package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Person;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class PersonRepositoryImpl implements PersonRepository {

    @Override
    public Person save(Person entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(Person entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<Person> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            Person person = entityManager.find(Person.class, id);
            return Optional.ofNullable(person);
        } catch (Exception e) {
            log.error("Error occurred while finding person by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding person by ID", e);
        }
    }

    @Override
    public Optional<Person> findByExternalId(Long externalId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT p FROM Person p WHERE p.externalId = :externalId";
            Person result = entityManager.createQuery(jpql, Person.class)
                    .setParameter("externalId", externalId)
                    .getSingleResultOrNull();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error occurred while finding person by externalId: {}", externalId, e);
            throw new RuntimeException("Error occurred while finding person by externalId", e);
        }
    }

    @Override
    public List<Person> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all persons", e);
            throw new RuntimeException("Error occurred while fetching all persons", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            Person person = entityManager.find(Person.class, id);
            if (person != null) {
                entityManager.remove(person);
                log.info("Person deleted successfully with ID: {}", id);
            } else {
                log.error("Person with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}
