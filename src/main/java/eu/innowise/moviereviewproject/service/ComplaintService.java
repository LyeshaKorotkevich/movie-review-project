package eu.innowise.moviereviewproject.service;

import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.dto.response.ComplaintResponse;
import eu.innowise.moviereviewproject.exceptions.complaint.ComplaintNotFoundException;
import eu.innowise.moviereviewproject.exceptions.review.ReviewNotFoundException;
import eu.innowise.moviereviewproject.exceptions.user.UserNotFoundException;
import eu.innowise.moviereviewproject.mapper.ComplaintMapper;
import eu.innowise.moviereviewproject.model.Complaint;
import eu.innowise.moviereviewproject.model.Review;
import eu.innowise.moviereviewproject.model.User;
import eu.innowise.moviereviewproject.model.enums.ComplaintStatus;
import eu.innowise.moviereviewproject.repository.ComplaintRepository;
import eu.innowise.moviereviewproject.repository.ReviewRepository;
import eu.innowise.moviereviewproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ComplaintMapper complaintMapper;

    public ComplaintService(ComplaintRepository complaintRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.complaintMapper = Mappers.getMapper(ComplaintMapper.class);
    }

    public void saveComplaint(ComplaintRequest complaintRequest) {
        User user = userRepository.findById(complaintRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Review review = reviewRepository.findById(complaintRequest.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        Complaint complaint = complaintMapper.toComplaint(complaintRequest);
        complaint.setUser(user);
        complaint.setReview(review);
        complaintRepository.save(complaint);

        log.info("Complaint saved successfully.");
    }

    public List<ComplaintResponse> getAllComplaints() {
        List<ComplaintResponse> complaints = complaintRepository.findAll()
                .stream()
                .map(complaintMapper::toResponse)
                .toList();

        log.info("{} complaints loaded from the database.", complaints.size());
        return complaints;
    }

    public List<ComplaintResponse> getComplaintsByStatus(String status) {
        List<ComplaintResponse> complaints = complaintRepository.findByStatus(status)
                .stream()
                .map(complaintMapper::toResponse)
                .toList();

        log.info("{} complaints with status '{}' loaded from the database.", complaints.size(), status);
        return complaints;
    }

    public void updateComplaintStatus(UUID complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found"));

        complaint.setStatus(Enum.valueOf(ComplaintStatus.class, status));
        complaintRepository.update(complaint);

        log.info("Complaint status updated to '{}' for complaint ID: {}", status, complaintId);
    }

    public void deleteComplaint(UUID complaintId) {
        complaintRepository.deleteById(complaintId);
        log.info("Complaint with ID: {} deleted successfully.", complaintId);
    }
}
