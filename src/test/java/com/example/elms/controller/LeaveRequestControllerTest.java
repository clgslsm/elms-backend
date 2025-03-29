package com.example.elms.controller;

import com.example.elms.entity.LeaveRequest;
import com.example.elms.service.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LeaveRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LeaveRequestService leaveRequestService;

    @InjectMocks
    private LeaveRequestController leaveRequestController;

    @Test
    void testGetAllLeaveRequests() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController).build();
        LeaveRequest leaveRequest = LeaveRequest.builder().id(1L).reason("Vacation").build();
        Mockito.when(leaveRequestService.getAllLeaveRequest()).thenReturn(List.of(leaveRequest));

        mockMvc.perform(get("/api/leave-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].reason").value("Vacation"));
    }

    @Test
    void testGetLeaveRequestById() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController).build();
        LeaveRequest leaveRequest = LeaveRequest.builder().id(1L).reason("Vacation").build();
        Mockito.when(leaveRequestService.getLeaveRequestById(1L)).thenReturn(leaveRequest);

        mockMvc.perform(get("/api/leave-requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reason").value("Vacation"));
    }

    @Test
    void testCreateLeaveRequest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController).build();
        LeaveRequest leaveRequest = LeaveRequest.builder().id(1L).reason("Vacation").build();
        Mockito.when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class), any())).thenReturn(leaveRequest);

        mockMvc.perform(post("/api/leave-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "idUserReceive": 2,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-10",
                            "reason": "Vacation",
                            "leaveType": "Annual",
                            "attachmentUrls": []
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reason").value("Vacation"));
    }

    @Test
    void testUpdateLeaveRequest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController).build();
        LeaveRequest leaveRequest = LeaveRequest.builder().id(1L).reason("Updated Reason").build();
        Mockito.when(leaveRequestService.updateLeaveRequest(anyLong(), any(LeaveRequest.class), any())).thenReturn(leaveRequest);

        mockMvc.perform(put("/api/leave-requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "idUserReceive": 2,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-10",
                            "reason": "Updated Reason",
                            "leaveType": "Annual",
                            "attachmentUrls": []
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reason").value("Updated Reason"));
    }

    @Test
    void testDeleteLeaveRequest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveRequestController).build();
        Mockito.doNothing().when(leaveRequestService).deleteLeaveRequest(1L);

        mockMvc.perform(delete("/api/leave-requests/1"))
                .andExpect(status().isNoContent());
    }
}
