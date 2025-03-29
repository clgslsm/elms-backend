package com.example.elms.service;

import com.example.elms.entity.LeaveRequest;
import com.example.elms.entity.User;
import com.example.elms.entity.Attachment;
import com.example.elms.exception.UnknownRoleException;
import com.example.elms.exception.ResourceNotFoundException;
import com.example.elms.exception.UnauthorizedAccessException;
import com.example.elms.exception.UserNotFound;
import com.example.elms.repository.LeaveRequestRepository;
import com.example.elms.repository.AttachmentRepository;
import com.example.elms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveRequestService {
    public final int EMPLOYEE = 1;
    public final int MANAGER = 2;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    
    public List<LeaveRequest> getAllLeaveRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        switch (user.getIdRole().intValue()){
            case EMPLOYEE:
                return leaveRequestRepository.findAllByIdUserSend(user.getId());
            case MANAGER:
                return leaveRequestRepository.findAllByIdUserReceive(user.getId());
            default:
                throw new UnknownRoleException("Unknown this role");
        }
    }
    
    public LeaveRequest getLeaveRequestById(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));
        
        // Verify user has permission to view this request
        boolean hasAccess = (user.getIdRole().intValue() == EMPLOYEE && leaveRequest.getIdUserSend().equals(user.getId())) ||
                          (user.getIdRole().intValue() == MANAGER && leaveRequest.getIdUserReceive().equals(user.getId()));
        
        if (!hasAccess) {
            throw new UnauthorizedAccessException("You don't have permission to view this leave request");
        }
        
        return leaveRequest;
    }
    
    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest, List<String> attachmentUrls) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        // Only employees can create leave requests
        if (user.getIdRole().intValue() != EMPLOYEE) {
            throw new UnauthorizedAccessException("Only employees can create leave requests");
        }
        
        // Set the sender as the current user
        leaveRequest.setIdUserSend(user.getId());
        leaveRequest.setCreatedAt(LocalDateTime.now());
        leaveRequest.setStatus("PENDING");
        
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        
        // Save attachments if provided
        if (attachmentUrls != null && !attachmentUrls.isEmpty()) {
            saveAttachments(savedRequest.getId(), attachmentUrls);
        }
        
        return savedRequest;
    }
    
    @Transactional
    public LeaveRequest updateLeaveRequest(Long requestId, LeaveRequest updatedRequest, List<String> attachmentUrls) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        LeaveRequest existingRequest = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));
        
        // Only the employee who created the request can update it, and only if it's still pending
        if (!existingRequest.getIdUserSend().equals(user.getId())) {
            throw new UnauthorizedAccessException("You can only edit your own leave requests");
        }
        
        if (!"PENDING".equals(existingRequest.getStatus())) {
            throw new UnauthorizedAccessException("You can only edit pending leave requests");
        }
        
        // Update fields
        existingRequest.setStartDate(updatedRequest.getStartDate());
        existingRequest.setEndDate(updatedRequest.getEndDate());
        existingRequest.setReason(updatedRequest.getReason());
        existingRequest.setLeaveType(updatedRequest.getLeaveType());
        existingRequest.setUpdatedAt(LocalDateTime.now());
        
        LeaveRequest savedRequest = leaveRequestRepository.save(existingRequest);
        
        // Update attachments if provided
        if (attachmentUrls != null) {
            // Remove existing attachments
            attachmentRepository.deleteAllByLeaveRequestId(requestId);
            
            // Add new attachments
            if (!attachmentUrls.isEmpty()) {
                saveAttachments(savedRequest.getId(), attachmentUrls);
            }
        }
        
        return savedRequest;
    }
    
    @Transactional
    public void deleteLeaveRequest(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));
        
        // Only the employee who created the request can delete it, and only if it's still pending
        if (!leaveRequest.getIdUserSend().equals(user.getId())) {
            throw new UnauthorizedAccessException("You can only delete your own leave requests");
        }
        
        if (!"PENDING".equals(leaveRequest.getStatus())) {
            throw new UnauthorizedAccessException("You can only delete pending leave requests");
        }
        
        // Delete attachments first
        attachmentRepository.deleteAllByLeaveRequestId(requestId);
        
        // Delete the leave request
        leaveRequestRepository.deleteById(requestId);
    }
    
    @Transactional
    public LeaveRequest approveLeaveRequest(Long requestId) {
        return updateLeaveRequestStatus(requestId, "APPROVED");
    }
    
    @Transactional
    public LeaveRequest declineLeaveRequest(Long requestId) {
        return updateLeaveRequestStatus(requestId, "DECLINED");
    }
    
    private LeaveRequest updateLeaveRequestStatus(Long requestId, String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        
        // Only managers can approve/decline
        if (user.getIdRole().intValue() != MANAGER) {
            throw new UnauthorizedAccessException("Only managers can approve or decline leave requests");
        }
        
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));
        
        // Ensure this manager is the intended recipient
        if (!leaveRequest.getIdUserReceive().equals(user.getId())) {
            throw new UnauthorizedAccessException("You can only process leave requests addressed to you");
        }
        
        // Only pending requests can be approved/declined
        if (!"PENDING".equals(leaveRequest.getStatus())) {
            throw new UnauthorizedAccessException("You can only process pending leave requests");
        }
        
        leaveRequest.setStatus(status);
        leaveRequest.setUpdatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(leaveRequest);
    }
    
    private void saveAttachments(Long leaveRequestId, List<String> attachmentUrls) {
        List<Attachment> attachments = attachmentUrls.stream()
            .map(url -> Attachment.builder()
                .leaveRequestId(leaveRequestId)
                .url(url)
                .createdAt(LocalDateTime.now())
                .build())
            .collect(Collectors.toList());
        
        attachmentRepository.saveAll(attachments);
    }
    
    public List<Attachment> getAttachmentsByLeaveRequestId(Long leaveRequestId) {
        // First check if the user has permission to access this leave request
        getLeaveRequestById(leaveRequestId);
        
        // If no exception was thrown, user has permission to view attachments
        return attachmentRepository.findAllByLeaveRequestId(leaveRequestId);
    }
    public List<LeaveRequest> getLeaveRequestsForManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }

        // Only managers can access this endpoint
        if (user.getIdRole().intValue() != MANAGER) {
            throw new UnauthorizedAccessException("Only managers can access this resource");
        }

        // Fetch all leave requests sent to this manager
        return leaveRequestRepository.findAllByIdUserReceive(user.getId());
    }
}