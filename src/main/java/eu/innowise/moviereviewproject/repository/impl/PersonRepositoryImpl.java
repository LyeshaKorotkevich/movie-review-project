package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Person;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.PersonRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class PersonRepositoryImpl extends AbstractHibernateDao<Person, UUID> implements PersonRepository {

    private PersonRepositoryImpl() {
        super(Person.class);
    }

    private static class SingletonHelper {
        private static final PersonRepositoryImpl INSTANCE = new PersonRepositoryImpl();
    }

    public static PersonRepositoryImpl getInstance() {
        return PersonRepositoryImpl.SingletonHelper.INSTANCE;
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
}
