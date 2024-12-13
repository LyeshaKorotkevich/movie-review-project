package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Complaint;
import eu.innowise.moviereviewproject.repository.ComplaintRepository;
import eu.innowise.moviereviewproject.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ComplaintRepositoryImpl implements ComplaintRepository {

    @Override
    public Complaint save(Complaint entity) {
        return executeInTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(Complaint entity) {
        executeInTransaction(entityManager -> {
            entityManager.merge(entity);
            return null;
        });
    }

    @Override
    public Optional<Complaint> findById(UUID id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(Complaint.class, id));
        } catch (Exception e) {
            log.error("Error occurred while finding complaint by ID: {}", id, e);
            throw new RuntimeException("Error occurred while finding complaint by ID", e);
        }
    }

    @Override
    public List<Complaint> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT c FROM Complaint c", Complaint.class).getResultList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all complaints", e);
            throw new RuntimeException("Error occurred while fetching all complaints", e);
        }
    }

    @Override
    public List<Complaint> findByStatus(String status) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            String jpql = "SELECT c FROM Complaint c WHERE c.status = :status";
            return entityManager.createQuery(jpql, Complaint.class)
                    .setParameter("status", status)
                    .getResultList();
        }
    }

    @Override
    public void deleteById(UUID id) {
        executeInTransaction(entityManager -> {
            Complaint complaint = entityManager.find(Complaint.class, id);
            if (complaint != null) {
                entityManager.remove(complaint);
                log.info("Complaint deleted successfully with ID: {}", id);
            } else {
                log.warn("Complaint with ID: {} not found for deletion", id);
            }
            return null;
        });
    }
}
