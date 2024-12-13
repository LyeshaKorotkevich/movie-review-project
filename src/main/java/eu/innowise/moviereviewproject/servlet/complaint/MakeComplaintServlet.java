package eu.innowise.moviereviewproject.servlet.complaint;

import eu.innowise.moviereviewproject.config.ApplicationConfig;
import eu.innowise.moviereviewproject.dto.request.ComplaintRequest;
import eu.innowise.moviereviewproject.service.ComplaintService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/make-complaint")
public class MakeComplaintServlet extends HttpServlet {

    private ComplaintService complaintService;

    @Override
    public void init() throws ServletException {
        complaintService = ApplicationConfig.getComplaintService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UUID userId = UUID.fromString(req.getParameter("userId"));
            UUID reviewId = UUID.fromString(req.getParameter("reviewId"));
            String reason = req.getParameter("reason");

            ComplaintRequest complaintRequest = new ComplaintRequest(userId, reviewId, reason);
            complaintService.saveComplaint(complaintRequest);

            resp.sendRedirect(req.getContextPath() + "/complaints");
        } catch (Exception e) {
            req.setAttribute("error", "Error processing complaint: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(req, resp);
        }
    }
}
