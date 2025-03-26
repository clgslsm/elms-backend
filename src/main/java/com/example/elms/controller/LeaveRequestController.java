package com.example.elms.controller;

import com.example.elms.dto.LeaveRequestDTO;
import com.example.elms.entity.Attachment;
import com.example.elms.entity.LeaveRequest;
import com.example.elms.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Requests", description = "Leave Request API")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @Operation(summary = "Get all leave requests", description = "Returns all leave requests based on user role",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequest());
    }

    @Operation(summary = "Get leave request by ID", description = "Returns a leave request by its ID",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(id));
    }

    @Operation(summary = "Create a new leave request", description = "Creates a new leave request with optional attachments",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .idUserReceive(leaveRequestDTO.getIdUserReceive())
                .startDate(leaveRequestDTO.getStartDate())
                .endDate(leaveRequestDTO.getEndDate())
                .reason(leaveRequestDTO.getReason())
                .leaveType(leaveRequestDTO.getLeaveType())
                .build();
                
        return new ResponseEntity<>(
                leaveRequestService.createLeaveRequest(leaveRequest, leaveRequestDTO.getAttachmentUrls()),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update a leave request", description = "Updates an existing leave request",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(
            @PathVariable Long id,
            @RequestBody LeaveRequestDTO leaveRequestDTO) {
        
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .idUserReceive(leaveRequestDTO.getIdUserReceive())
                .startDate(leaveRequestDTO.getStartDate())
                .endDate(leaveRequestDTO.getEndDate())
                .reason(leaveRequestDTO.getReason())
                .leaveType(leaveRequestDTO.getLeaveType())
                .build();
                
        return ResponseEntity.ok(
                leaveRequestService.updateLeaveRequest(id, leaveRequest, leaveRequestDTO.getAttachmentUrls())
        );
    }

    @Operation(summary = "Delete a leave request", description = "Deletes a leave request by its ID",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Approve a leave request", description = "Approves a pending leave request",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.approveLeaveRequest(id));
    }

    @Operation(summary = "Decline a leave request", description = "Declines a pending leave request",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/{id}/decline")
    public ResponseEntity<LeaveRequest> declineLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.declineLeaveRequest(id));
    }

    @Operation(summary = "Get attachments for a leave request", description = "Returns all attachments for a leave request",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/{id}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getAttachmentsByLeaveRequestId(id));
    }
}