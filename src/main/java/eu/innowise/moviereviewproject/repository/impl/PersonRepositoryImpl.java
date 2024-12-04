package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Person;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class PersonRepositoryImpl implements PersonRepository {

    @Override
    public Optional<Person> findByExternalId(Long externalId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT p FROM Person p WHERE p.externalId = :externalId";
            List<Person> result = entityManager.createQuery(jpql, Person.class)
                    .setParameter("externalId", externalId)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception e) {
            log.error("Error occurred while finding person by externalId: {}", externalId, e);
            throw new RuntimeException("Error occurred while finding person by externalId", e);
        }
    }

    @Override
    public Person save(Person entity) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            log.info("Person saved successfully: {}", entity.getExternalId());
            return entity;
        } catch (Exception e) {
            log.error("Error occurred while saving person: {}", entity.getExternalId(), e);
            throw new RuntimeException("Error occurred while saving person", e);
        }
    }

    @Override
    public void update(Person entity) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            log.info("Person updated successfully: {}", entity.getExternalId());
        } catch (Exception e) {
            log.error("Error occurred while updating person: {}", entity.getExternalId(), e);
            throw new RuntimeException("Error occurred while updating person", e);
        }
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
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Person person = entityManager.find(Person.class, id);
            if (person != null) {
                entityManager.remove(person);
                log.info("Person deleted successfully with ID: {}", id);
            } else {
                log.warn("Person with ID: {} not found for deletion", id);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error occurred while deleting person with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting person", e);
        }
    }
}
