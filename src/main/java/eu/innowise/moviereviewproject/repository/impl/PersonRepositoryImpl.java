package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Person;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonRepositoryImpl implements PersonRepository {

    private final EntityManager entityManager = JpaUtil.getEntityManager();

    @Override
    public Optional<Person> findByExternalId(Long externalId) {
        String jpql = "SELECT p FROM Person p WHERE p.externalId = :externalId";
        List<Person> result = entityManager.createQuery(jpql, Person.class)
                .setParameter("externalId", externalId)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }


    @Override
    public Person save(Person entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }


    @Override
    public void update(Person entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public Optional<Person> findById(UUID id) {
        Person person = entityManager.find(Person.class, id);
        return Optional.ofNullable(person);
    }

    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Override
    public void deleteById(UUID id) {
        entityManager.getTransaction().begin();
        Person person = entityManager.find(Person.class, id);
        if (person != null) {
            entityManager.remove(person);
        }
        entityManager.getTransaction().commit();
    }
}
