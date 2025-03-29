package com.example.elms.service;

import com.example.elms.entity.LeaveRequest;
import com.example.elms.entity.Attachment;
import com.example.elms.exception.ResourceNotFoundException;
import com.example.elms.repository.LeaveRequestRepository;
import com.example.elms.repository.AttachmentRepository;
import com.example.elms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllLeaveRequests() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(1L)
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Vacation")
                .build();
        when(leaveRequestRepository.findAll()).thenReturn(Collections.singletonList(leaveRequest));

        List<LeaveRequest> result = leaveRequestService.getAllLeaveRequest();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Vacation", result.get(0).getReason());
    }

    @Test
    void testGetLeaveRequestById_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(1L)
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Vacation")
                .build();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        LeaveRequest result = leaveRequestService.getLeaveRequestById(1L);
        assertNotNull(result);
        assertEquals("Vacation", result.getReason());
    }

    @Test
    void testGetLeaveRequestById_NotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leaveRequestService.getLeaveRequestById(1L));
    }

    @Test
    void testCreateLeaveRequest() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Vacation")
                .build();
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        LeaveRequest result = leaveRequestService.createLeaveRequest(leaveRequest, Collections.emptyList());
        assertNotNull(result);
        assertEquals("Vacation", result.getReason());
    }

    @Test
    void testUpdateLeaveRequest_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest existingLeaveRequest = LeaveRequest.builder()
                .id(1L)
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Vacation")
                .build();
        LeaveRequest updatedLeaveRequest = LeaveRequest.builder()
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Updated Reason")
                .build();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(existingLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(updatedLeaveRequest);

        LeaveRequest result = leaveRequestService.updateLeaveRequest(1L, updatedLeaveRequest, Collections.emptyList());
        assertNotNull(result);
        assertEquals("Updated Reason", result.getReason());
    }

    @Test
    void testUpdateLeaveRequest_NotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest updatedLeaveRequest = LeaveRequest.builder()
                .idUserSend(1L)
                .idUserReceive(2L)
                .reason("Updated Reason")
                .build();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leaveRequestService.updateLeaveRequest(1L, updatedLeaveRequest, Collections.emptyList()));
    }

    @Test
    void testDeleteLeaveRequest_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        LeaveRequest leaveRequest = LeaveRequest.builder().id(1L).build();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));
        doNothing().when(leaveRequestRepository).deleteById(1L);

        leaveRequestService.deleteLeaveRequest(1L);
        verify(leaveRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLeaveRequest_NotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leaveRequestService.deleteLeaveRequest(1L));
    }

    @Test
    void testGetAttachmentsByLeaveRequestId() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        Attachment attachment = Attachment.builder()
                .id(1L)
                .leaveRequestId(1L)
                .url("http://example.com/file.pdf")
                .build();
        when(attachmentRepository.findAllByLeaveRequestId(1L)).thenReturn(Collections.singletonList(attachment));

        List<Attachment> result = leaveRequestService.getAttachmentsByLeaveRequestId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("http://example.com/file.pdf", result.get(0).getUrl());
    }
}
