package eu.innowise.moviereviewproject.repository.impl;

import eu.innowise.moviereviewproject.model.Complaint;
import eu.innowise.moviereviewproject.repository.AbstractHibernateDao;
import eu.innowise.moviereviewproject.repository.ComplaintRepository;
import eu.innowise.moviereviewproject.utils.db.JpaUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static eu.innowise.moviereviewproject.utils.Constants.SELECT_COMPLAINTS_BY_STATUS;

@Slf4j
public class ComplaintRepositoryImpl extends AbstractHibernateDao<Complaint, UUID> implements ComplaintRepository {

    private ComplaintRepositoryImpl() {
        super(Complaint.class);
    }

    private static class SingletonHelper {
        private static final ComplaintRepositoryImpl INSTANCE = new ComplaintRepositoryImpl();
    }

    public static ComplaintRepositoryImpl getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public List<Complaint> findByStatus(String status) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery(SELECT_COMPLAINTS_BY_STATUS, Complaint.class)
                    .setParameter("status", status)
                    .getResultList();
        }
    }
}
