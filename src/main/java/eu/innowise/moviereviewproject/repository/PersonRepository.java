package eu.innowise.moviereviewproject.repository;

import eu.innowise.moviereviewproject.model.Person;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person> {
    Optional<Person> findByExternalId(Long externalId);
}
